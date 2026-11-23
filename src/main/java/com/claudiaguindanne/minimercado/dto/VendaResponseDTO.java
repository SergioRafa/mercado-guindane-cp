package com.claudiaguindanne.minimercado.dto;

import com.claudiaguindanne.minimercado.model.Pedido;
import com.claudiaguindanne.minimercado.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
        Long id,
        String clienteNome,
        LocalDateTime dataHora,
        StatusPedido status,
        BigDecimal valorTotal,
        List<ItemVendaResponseDTO> itens
) {
    public static VendaResponseDTO fromEntity(Pedido pedido) {
        List<ItemVendaResponseDTO> itensDto = pedido.getItens().stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();
        return new VendaResponseDTO(
                pedido.getId(),
                pedido.getClienteNome(),
                pedido.getDataHora(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                itensDto
        );
    }
}
