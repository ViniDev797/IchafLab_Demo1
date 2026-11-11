package com.IchafLab.backend.controller;

import com.IchafLab.backend.dto.ItemPedidoRequest;
import com.IchafLab.backend.dto.ItemPedidoResponse;
import com.IchafLab.backend.service.ItemPedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/itens")
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    // Injeção de dependência por construtor:
    // o Spring entrega o service pronto quando cria o controller.
    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    // POST /api/pedidos/{pedidoId}/itens
    // Adiciona um produto ao pedido. O preço é definido pelo backend.
    @PostMapping
    public ResponseEntity<ItemPedidoResponse> adicionarItem(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequest request) {
        ItemPedidoResponse criado = itemPedidoService.adicionarItem(pedidoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // GET /api/pedidos/{pedidoId}/itens
    // Lista todos os itens do pedido.
    @GetMapping
    public ResponseEntity<List<ItemPedidoResponse>> listarPorPedido(@PathVariable Long pedidoId) {
        List<ItemPedidoResponse> itens = itemPedidoService.listarPorPedido(pedidoId);
        return ResponseEntity.ok(itens);
    }
}
