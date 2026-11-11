package com.IchafLab.backend.controller;

import com.IchafLab.backend.entity.Categoria;
import com.IchafLab.backend.service.CategoriaService;
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
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Injeção de dependência por construtor:
    // o Spring entrega o service pronto quando cria o controller.
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /api/categorias
    // Lista todas as categorias.
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodos() {
        List<Categoria> categorias = categoriaService.listarTodos();
        return ResponseEntity.ok(categorias);
    }

    // GET /api/categorias/{id}
    // Busca uma categoria pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    // POST /api/categorias
    // Cadastra uma nova categoria com os dados enviados no corpo (JSON).
    @PostMapping
    public ResponseEntity<Categoria> cadastrar(@Valid @RequestBody Categoria categoria) {
        Categoria categoriaSalva = categoriaService.cadastrar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    // PUT /api/categorias/{id}
    // Atualiza a categoria com o ID informado.
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id,
                                               @Valid @RequestBody Categoria categoria) {
        Categoria categoriaAtualizada = categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    // DELETE /api/categorias/{id}
    // Exclui a categoria com o ID informado.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
