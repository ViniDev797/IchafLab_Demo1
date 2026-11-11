package com.IchafLab.backend.dto;

import com.IchafLab.backend.entity.Categoria;
import com.IchafLab.backend.entity.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean disponivel,
        Long categoriaId,
        String categoriaNome
) {

    // Converte a entidade Produto neste formato de resposta.
    // Produtos antigos, sem categoria, ficam com categoriaId e categoriaNome nulos.
    public static ProdutoResponse deEntidade(Produto produto) {
        Categoria categoria = produto.getCategoria();

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getDisponivel(),
                categoria != null ? categoria.getId() : null,
                categoria != null ? categoria.getNome() : null
        );
    }
}
