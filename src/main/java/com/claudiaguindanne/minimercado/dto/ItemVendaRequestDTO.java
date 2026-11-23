package com.claudiaguindanne.minimercado.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemVendaRequestDTO(
        @NotNull(message = "ID do produto e obrigatorio") Long produtoId,
        @NotNull(message = "Quantidade e obrigatoria") @Positive(message = "Quantidade deve ser maior que zero") Integer quantidade
) {
}
