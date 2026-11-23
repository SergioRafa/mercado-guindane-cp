package com.claudiaguindanne.minimercado.dto;

import com.claudiaguindanne.minimercado.model.ItemPedido;
import java.math.BigDecimal;

public record ItemVendaResponseDTO(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemVendaResponseDTO fromEntity(ItemPedido item) {
        return new ItemVendaResponseDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
