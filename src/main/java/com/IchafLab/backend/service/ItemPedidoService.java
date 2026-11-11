package com.IchafLab.backend.service;

import com.IchafLab.backend.dto.ItemPedidoRequest;
import com.IchafLab.backend.dto.ItemPedidoResponse;
import com.IchafLab.backend.entity.ItemPedido;
import com.IchafLab.backend.entity.Pedido;
import com.IchafLab.backend.entity.Produto;
import com.IchafLab.backend.repository.ItemPedidoRepository;
import com.IchafLab.backend.repository.PedidoRepository;
import com.IchafLab.backend.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    // Injeção de dependência por construtor:
    // o Spring entrega os três repositories prontos.
    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository,
                             PedidoRepository pedidoRepository,
                             ProdutoRepository produtoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    // Adiciona um produto a um pedido e atualiza o valor total do pedido.
    // O preço unitário vem SEMPRE do produto no banco, nunca do cliente.
    // Tudo acontece em uma única transação: se algo falhar, nada é gravado.
    @Transactional
    public ItemPedidoResponse adicionarItem(Long pedidoId, ItemPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("O item do pedido não pode ser nulo.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Pedido não encontrado com o id: " + pedidoId));

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Produto não encontrado com o id: " + request.produtoId()));

        // Cópia do preço do produto neste momento (preserva o histórico).
        ItemPedido item = new ItemPedido(
                pedido,
                produto,
                request.quantidade(),
                produto.getPreco());

        ItemPedido itemSalvo = itemPedidoRepository.save(item);

        // Busca TODOS os itens do pedido (incluindo o recém-salvo)
        // e soma os subtotais, começando de zero.
        List<ItemPedido> itensDoPedido = itemPedidoRepository.findByPedidoId(pedidoId);

        BigDecimal totalCalculado = BigDecimal.ZERO;
        for (ItemPedido itemDoPedido : itensDoPedido) {
            totalCalculado = totalCalculado.add(itemDoPedido.getSubtotal());
        }

        // O pedido está gerenciado pelo JPA nesta transação:
        // a alteração é gravada automaticamente ao final dela.
        pedido.atualizarValorTotal(totalCalculado);

        return ItemPedidoResponse.deEntidade(itemSalvo);
    }

    // Lista os itens de um pedido, já convertidos para ItemPedidoResponse.
    // A conversão acontece dentro da transação, pois ler o nome do produto
    // depende do carregamento LAZY.
    @Transactional(readOnly = true)
    public List<ItemPedidoResponse> listarPorPedido(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new NoSuchElementException(
                    "Pedido não encontrado com o id: " + pedidoId);
        }

        return itemPedidoRepository.findByPedidoId(pedidoId).stream()
                .map(ItemPedidoResponse::deEntidade)
                .toList();
    }
}
