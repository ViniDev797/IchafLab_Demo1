package com.IchafLab.backend.service;

import com.IchafLab.backend.dto.ProdutoRequest;
import com.IchafLab.backend.dto.ProdutoResponse;
import com.IchafLab.backend.entity.Categoria;
import com.IchafLab.backend.entity.Produto;
import com.IchafLab.backend.repository.CategoriaRepository;
import com.IchafLab.backend.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    // Injeção de dependência por construtor:
    // o Spring entrega os dois repositories prontos.
    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Retorna todos os produtos já convertidos para ProdutoResponse.
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponse::deEntidade)
                .toList();
    }

    // Busca um produto pelo ID. Se não existir, lança NoSuchElementException (404).
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.deEntidade(buscarEntidadePorId(id));
    }

    // Cadastra um novo produto. A categoria é obrigatória apenas aqui.
    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest dados) {
        validarRequestNaoNulo(dados);

        // NOVO: no cadastro, o produto precisa ter uma categoria.
        if (dados.categoriaId() == null) {
            throw new IllegalArgumentException("A categoria do produto é obrigatória.");
        }

        Produto produto = new Produto(dados.nome(), dados.descricao(), dados.preco());

        // Se disponivel veio nulo, mantém o valor padrão da entidade (true).
        if (dados.disponivel() != null) {
            produto.setDisponivel(dados.disponivel());
        }

        // Se a categoria não existir, lança NoSuchElementException (404).
        produto.setCategoria(buscarCategoria(dados.categoriaId()));

        return ProdutoResponse.deEntidade(produtoRepository.save(produto));
    }

    // Atualiza um produto existente.
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest dados) {
        validarRequestNaoNulo(dados);

        Produto produto = buscarEntidadePorId(id); // lança exceção se não existir

        produto.setNome(dados.nome());
        produto.setDescricao(dados.descricao());
        produto.setPreco(dados.preco());

        // Se disponivel veio nulo, mantém o valor atual.
        if (dados.disponivel() != null) {
            produto.setDisponivel(dados.disponivel());
        }

        // Só troca a categoria se um categoriaId foi informado.
        // Sem categoriaId, a categoria atual (ou a ausência dela) é mantida.
        if (dados.categoriaId() != null) {
            produto.setCategoria(buscarCategoria(dados.categoriaId()));
        }

        return ProdutoResponse.deEntidade(produtoRepository.save(produto));
    }

    // Exclui um produto pelo ID. Se não existir, lança NoSuchElementException (404).
    @Transactional
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Produto não encontrado com o id: " + id);
        }
        produtoRepository.deleteById(id);
    }

    // Busca a entidade Produto (uso interno do service).
    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Produto não encontrado com o id: " + id));
    }

    // Busca a categoria pelo ID. Se não existir, lança NoSuchElementException,
    // que o GlobalExceptionHandler transforma em 404.
    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Categoria não encontrada com o id: " + categoriaId));
    }

    // As regras de nome e preço são conferidas pelas anotações do ProdutoRequest
    // (por meio do @Valid no controller). Aqui fica a proteção contra objeto nulo.
    private void validarRequestNaoNulo(ProdutoRequest dados) {
        if (dados == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
    }
}
