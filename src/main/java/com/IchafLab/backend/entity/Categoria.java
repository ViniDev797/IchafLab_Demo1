package com.IchafLab.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    private String descricao;

    @NotNull(message = "O campo ativa é obrigatório.")
    @Column(nullable = false)
    private Boolean ativa = true;

    // Construtor vazio: obrigatório para o JPA funcionar.
    public Categoria() {
    }

    // Construtor com os campos principais.
    // O campo "ativa" já começa como true.
    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
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

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}