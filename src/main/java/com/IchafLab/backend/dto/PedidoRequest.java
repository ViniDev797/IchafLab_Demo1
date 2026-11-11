package com.IchafLab.backend.dto;

import com.IchafLab.backend.enums.CanalOrigem;
import com.IchafLab.backend.enums.ModalidadePedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequest(

        @NotNull(message = "A modalidade do pedido é obrigatória.")
        ModalidadePedido modalidade,

        @NotNull(message = "O canal de origem é obrigatório.")
        CanalOrigem canalOrigem,

        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
        String observacao

) {
}