package com.claudiaguindanne.minimercado.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VendaRequestDTO(
        String clienteNome,
        @NotEmpty(message = "A venda deve conter ao menos um item") List<@Valid ItemVendaRequestDTO> itens
) {
}
