package com.IchafLab.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequest(

        @NotNull(message = "O produto é obrigatório.")
        @Positive(message = "O id do produto deve ser maior que zero.")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Integer quantidade
) {
}
