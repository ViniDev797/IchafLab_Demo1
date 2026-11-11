package com.IchafLab.backend.dto;

import com.IchafLab.backend.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoRequest(

        @NotNull(message = "O status do pedido é obrigatório.")
        StatusPedido status
) {
}
