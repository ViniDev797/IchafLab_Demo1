package com.IchafLab.backend.dto;

import com.IchafLab.backend.entity.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long id,
        Long pedidoId,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {

    // Converte a entidade ItemPedido neste formato de resposta.
    // Deve ser chamado dentro de uma transação (no service), porque pedido e produto
    // são carregados com LAZY e o nome do produto precisa ser lido do banco.
    public static ItemPedidoResponse deEntidade(ItemPedido item) {
        return new ItemPedidoResponse(
                item.getId(),
                item.getPedido().getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
