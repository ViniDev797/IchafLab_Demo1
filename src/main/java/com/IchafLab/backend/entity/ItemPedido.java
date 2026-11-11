package com.IchafLab.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    // Gerado pelo banco (AUTO_INCREMENT). Não existe setId de propósito.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos itens pertencem a um pedido.
    // A coluna pedido_id é a chave estrangeira e é obrigatória.
    @NotNull(message = "O pedido do item é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Muitos itens podem apontar para o mesmo produto.
    // A coluna produto_id é a chave estrangeira e é obrigatória.
    @NotNull(message = "O produto do item é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    @Column(nullable = false)
    private Integer quantidade;

    // Preço do produto NO MOMENTO em que foi incluído no pedido.
    // É uma cópia gravada no item: se o preço do produto mudar depois,
    // o histórico deste pedido continua correto.
    @NotNull(message = "O preço unitário é obrigatório.")
    @Positive(message = "O preço unitário deve ser maior que zero.")
    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    // Construtor vazio: obrigatório para o JPA funcionar.
    public ItemPedido() {
    }

    // Construtor com os campos principais.
    public ItemPedido(Pedido pedido,
                      Produto produto,
                      Integer quantidade,
                      BigDecimal precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Subtotal do item: quantidade x preço unitário.
    // Não é coluna do banco: é calculado sempre que for pedido.
    public BigDecimal getSubtotal() {
        if (quantidade == null || precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}

