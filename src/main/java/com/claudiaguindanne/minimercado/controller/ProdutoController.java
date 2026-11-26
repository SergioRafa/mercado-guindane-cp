package com.claudiaguindanne.minimercado.controller;

import com.claudiaguindanne.minimercado.model.Produto;
import com.claudiaguindanne.minimercado.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    @GetMapping("/estoque-baixo/{limite}")
    public ResponseEntity<List<Produto>> listarEstoqueBaixo(@PathVariable int limite) {
        return ResponseEntity.ok(produtoService.listarEstoqueBaixo(limite));
    }

    @GetMapping("/ordenados/margem")
    public ResponseEntity<List<Produto>> listarProdutosPorMargem() {
        return ResponseEntity.ok(produtoService.listarOrdenadosPorMargem());
    }

    @GetMapping("/valor-por-categoria")
    public ResponseEntity<Map<String, BigDecimal>> calcularValorPorCategoria() {
        return ResponseEntity.ok(produtoService.calcularValorPorCategoria());
    }

    @GetMapping("/valor-total-estoque")
    public ResponseEntity<BigDecimal> calcularValorTotalEstoque() {
        return ResponseEntity.ok(produtoService.calcularValorTotalEstoque());
    }

    @GetMapping("/financeiro/resumo")
    public ResponseEntity<Map<String, BigDecimal>> obterResumoFinanceiro() {
        return ResponseEntity.ok(produtoService.obterResumoFinanceiro());
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody @Valid Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody @Valid Produto produtoDetalhes) {
        return ResponseEntity.ok(produtoService.atualizar(id, produtoDetalhes));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<Produto> darBaixaEstoque(@PathVariable Long id, @RequestParam int quantidade) {
        return ResponseEntity.ok(produtoService.darBaixaEstoque(id, quantidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}