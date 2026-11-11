package com.IchafLab.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;      // NOVO
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;     // NOVO
import jakarta.persistence.ManyToOne;      // NOVO
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean disponivel = true;

    // NOVO: muitos produtos pertencem a uma categoria.
    // A coluna "categoria_id" guarda o ID da categoria (chave estrangeira).
    // Por enquanto é opcional (nullable = true) para preservar os produtos
    // que já existem no banco e ainda não têm categoria.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Produto() {
    }

    public Produto(String nome, String descricao, BigDecimal preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    // NOVO
    public Categoria getCategoria() {
        return categoria;
    }

    // NOVO
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}