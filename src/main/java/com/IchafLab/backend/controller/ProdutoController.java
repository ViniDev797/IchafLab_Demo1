package com.IchafLab.backend.controller;

import com.IchafLab.backend.dto.ProdutoRequest;
import com.IchafLab.backend.dto.ProdutoResponse;
import com.IchafLab.backend.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // Injeção de dependência por construtor:
    // o Spring entrega o service pronto quando cria o controller.
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET /api/produtos
    // Lista todos os produtos.
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        List<ProdutoResponse> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // GET /api/produtos/{id}
    // Busca um produto pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        ProdutoResponse produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    // POST /api/produtos
    // Cadastra um produto. O @Valid confere as anotações do ProdutoRequest.
    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest dados) {
        ProdutoResponse criado = produtoService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // PUT /api/produtos/{id}
    // Atualiza o produto com o ID informado.
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody ProdutoRequest dados) {
        ProdutoResponse atualizado = produtoService.atualizar(id, dados);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /api/produtos/{id}
    // Exclui o produto com o ID informado.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
