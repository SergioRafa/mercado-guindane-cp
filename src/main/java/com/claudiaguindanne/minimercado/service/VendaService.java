package com.claudiaguindanne.minimercado.service;

import com.claudiaguindanne.minimercado.dto.ItemVendaRequestDTO;
import com.claudiaguindanne.minimercado.dto.VendaRequestDTO;
import com.claudiaguindanne.minimercado.dto.VendaResponseDTO;
import com.claudiaguindanne.minimercado.exception.EstoqueInsuficienteException;
import com.claudiaguindanne.minimercado.model.ItemPedido;
import com.claudiaguindanne.minimercado.model.Pedido;
import com.claudiaguindanne.minimercado.model.Produto;
import com.claudiaguindanne.minimercado.model.StatusPedido;
import com.claudiaguindanne.minimercado.repository.PedidoRepository;
import com.claudiaguindanne.minimercado.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public VendaResponseDTO registrarVenda(VendaRequestDTO request) {
        Pedido pedido = new Pedido(request.clienteNome());

        for (ItemVendaRequestDTO itemReq : request.itens()) {
            Produto produto = produtoRepository.findById(itemReq.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado: ID " + itemReq.produtoId()));

            int estoqueAtual = produto.getQuantidadeEstoque() != null ? produto.getQuantidadeEstoque() : 0;
            if (estoqueAtual < itemReq.quantidade()) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para '" + produto.getNome() + "'. Disponivel: " + estoqueAtual
                );
            }

            produto.setQuantidadeEstoque(estoqueAtual - itemReq.quantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido(produto, itemReq.quantidade(), produto.getPrecoVenda());
            pedido.adicionarItem(itemPedido);
        }

        Pedido salvo = pedidoRepository.save(pedido);
        return VendaResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<VendaResponseDTO> listarTodas() {
        return pedidoRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<VendaResponseDTO> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(VendaResponseDTO::fromEntity);
    }

    @Transactional
    public VendaResponseDTO cancelarVenda(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado: ID " + id));

        if (pedido.getStatus() == StatusPedido.CANCELADA) {
            throw new IllegalArgumentException("Este pedido ja foi cancelado.");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            int atual = produto.getQuantidadeEstoque() != null ? produto.getQuantidadeEstoque() : 0;
            produto.setQuantidadeEstoque(atual + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(StatusPedido.CANCELADA);
        return VendaResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }
}