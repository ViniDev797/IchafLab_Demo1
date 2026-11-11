package com.IchafLab.backend.dto;

import com.IchafLab.backend.entity.Pedido;
import com.IchafLab.backend.enums.CanalOrigem;
import com.IchafLab.backend.enums.ModalidadePedido;
import com.IchafLab.backend.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        LocalDateTime dataHora,
        ModalidadePedido modalidade,
        StatusPedido status,
        CanalOrigem canalOrigem,
        String observacao,
        BigDecimal valorTotal
) {

    // Converte a entidade Pedido neste formato de resposta,
    // seguindo o mesmo padrão do ProdutoResponse.
    public static PedidoResponse deEntidade(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getDataHora(),
                pedido.getModalidade(),
                pedido.getStatus(),
                pedido.getCanalOrigem(),
                pedido.getObservacao(),
                pedido.getValorTotal()
        );
    }
}
