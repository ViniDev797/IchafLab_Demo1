package com.IchafLab.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// "record" é uma classe enxuta do Java para guardar dados: o compilador
// cria sozinho o construtor, os getters (nome(), preco() etc.), equals e toString.
public record ProdutoRequest(

        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        BigDecimal preco,

        Boolean disponivel,

        // Opcional: ID da categoria à qual o produto pertence.
        Long categoriaId
) {
}