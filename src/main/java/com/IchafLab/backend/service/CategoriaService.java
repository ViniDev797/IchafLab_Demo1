package com.IchafLab.backend.service;

import com.IchafLab.backend.entity.Categoria;
import com.IchafLab.backend.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    private static final int TAMANHO_MAXIMO_NOME = 100;
    private static final int TAMANHO_MAXIMO_DESCRICAO = 255;

    private final CategoriaRepository categoriaRepository;

    // Injeção de dependência por construtor:
    // o Spring entrega o repository pronto quando cria o service.
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Retorna todas as categorias cadastradas.
    @Transactional(readOnly = true)
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    // Busca uma categoria pelo ID. Se não existir, lança uma exceção.
    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Categoria não encontrada com o id: " + id));
    }

    // Cadastra uma nova categoria, desde que os dados sejam válidos.
    @Transactional
    public Categoria cadastrar(Categoria categoria) {
        validarCategoria(categoria);
        categoria.setId(null); // garante que será um INSERT, nunca uma atualização
        return categoriaRepository.save(categoria);
    }

    // Atualiza os dados de uma categoria que já existe.
    @Transactional
    public Categoria atualizar(Long id, Categoria dadosNovos) {
        validarCategoria(dadosNovos);

        Categoria categoriaExistente = buscarPorId(id); // lança exceção se não existir

        categoriaExistente.setNome(dadosNovos.getNome());
        categoriaExistente.setDescricao(dadosNovos.getDescricao());
        categoriaExistente.setAtiva(dadosNovos.getAtiva());

        return categoriaRepository.save(categoriaExistente);
    }

    // Exclui uma categoria pelo ID. Se não existir, lança uma exceção.
    @Transactional
    public void excluir(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Categoria não encontrada com o id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    // Reúne todas as validações da categoria.
    // É usada tanto no cadastro quanto na atualização.
    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("A categoria não pode ser nula.");
        }
        validarNome(categoria.getNome());
        validarDescricao(categoria.getDescricao());
        validarAtiva(categoria.getAtiva());
    }

    // O nome é obrigatório, não pode ser vazio nem só espaços
    // e aceita no máximo 100 caracteres.
    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException(
                    "O nome da categoria deve ter no máximo "
                            + TAMANHO_MAXIMO_NOME + " caracteres.");
        }
    }

    // A descrição é opcional, mas, se existir, aceita no máximo 255 caracteres.
    private void validarDescricao(String descricao) {
        if (descricao != null && descricao.length() > TAMANHO_MAXIMO_DESCRICAO) {
            throw new IllegalArgumentException(
                    "A descrição deve ter no máximo "
                            + TAMANHO_MAXIMO_DESCRICAO + " caracteres.");
        }
    }

    // O campo ativa não pode ser nulo.
    private void validarAtiva(Boolean ativa) {
        if (ativa == null) {
            throw new IllegalArgumentException("O campo ativa é obrigatório.");
        }
    }
}
