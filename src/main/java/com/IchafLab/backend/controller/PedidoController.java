package com.IchafLab.backend.controller;

import com.IchafLab.backend.dto.AtualizarStatusPedidoRequest;
import com.IchafLab.backend.dto.PedidoRequest;
import com.IchafLab.backend.dto.PedidoResponse;
import com.IchafLab.backend.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    // Injeção de dependência por construtor:
    // o Spring entrega o service pronto quando cria o controller.
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET /api/pedidos
    // Lista todos os pedidos.
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    // GET /api/pedidos/{id}
    // Busca um pedido pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    // POST /api/pedidos
    // Cadastra um pedido. O @Valid confere as anotações do PedidoRequest.
    @PostMapping
    public ResponseEntity<PedidoResponse> cadastrar(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse criado = pedidoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // PATCH /api/pedidos/{id}/status
    // Atualiza somente o status do pedido. As regras de transição
    // ficam no PedidoService; o controller apenas repassa a requisição.
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPedidoRequest request) {
        PedidoResponse atualizado = pedidoService.atualizarStatus(id, request);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /api/pedidos/{id}
    // Exclui o pedido com o ID informado.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}