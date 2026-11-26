package com.claudiaguindanne.minimercado.service;

import com.claudiaguindanne.minimercado.exception.RecursoNaoEncontradoException;
import com.claudiaguindanne.minimercado.model.Produto;
import com.claudiaguindanne.minimercado.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto com id " + id + " nao encontrado."
                ));
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<Produto> listarEstoqueBaixo(int limite) {
        return produtoRepository.findByQuantidadeEstoqueLessThanEqual(limite);
    }

    public List<Produto> listarOrdenadosPorMargem() {
        return produtoRepository.findAll().stream()
                .sorted(Comparator.comparing(Produto::getMargemLucroPercentual).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, BigDecimal> calcularValorPorCategoria() {
        return produtoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Produto::getCategoria,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                p -> p.getValorTotalEstoque() != null ? p.getValorTotalEstoque() : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                                Collectors.reducing(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), BigDecimal::add)
                        )
                ));
    }

    public BigDecimal calcularValorTotalEstoque() {
        return produtoRepository.findAll().stream()
                .map(p -> p.getValorTotalEstoque() != null ? p.getValorTotalEstoque() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> obterResumoFinanceiro() {
        List<Produto> produtos = produtoRepository.findAll();

        BigDecimal custoTotal = produtos.stream()
                .map(p -> {
                    BigDecimal custo = p.getPrecoCusto() != null ? p.getPrecoCusto() : BigDecimal.ZERO;
                    int qtd = p.getQuantidadeEstoque() != null ? p.getQuantidadeEstoque() : 0;
                    return custo.multiply(BigDecimal.valueOf(qtd));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal vendaTotal = produtos.stream()
                .map(p -> p.getValorTotalEstoque() != null ? p.getValorTotalEstoque() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal lucroPotencial = vendaTotal.subtract(custoTotal).setScale(2, RoundingMode.HALF_UP);

        Map<String, BigDecimal> resumo = new LinkedHashMap<>();
        resumo.put("custoTotalEstoque", custoTotal);
        resumo.put("vendaTotalEstoque", vendaTotal);
        resumo.put("lucroPotencialEstoque", lucroPotencial);
        return resumo;
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoDetalhes) {
        Produto existente = buscarPorId(id);
        existente.setNome(produtoDetalhes.getNome());
        existente.setCategoria(produtoDetalhes.getCategoria());
        existente.setPrecoCusto(produtoDetalhes.getPrecoCusto());
        existente.setPrecoVenda(produtoDetalhes.getPrecoVenda());
        existente.setQuantidadeEstoque(produtoDetalhes.getQuantidadeEstoque());
        return produtoRepository.save(existente);
    }

    public Produto darBaixaEstoque(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade para baixa deve ser maior que zero.");
        }
        Produto existente = buscarPorId(id);
        int atual = existente.getQuantidadeEstoque() != null ? existente.getQuantidadeEstoque() : 0;
        if (atual < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto informado.");
        }
        existente.setQuantidadeEstoque(atual - quantidade);
        return produtoRepository.save(existente);
    }

    public void deletar(Long id) {
        Produto existente = buscarPorId(id);
        produtoRepository.delete(existente);
    }
}