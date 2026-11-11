package com.IchafLab.backend.entity;

import com.IchafLab.backend.enums.CanalOrigem;
import com.IchafLab.backend.enums.ModalidadePedido;
import com.IchafLab.backend.enums.StatusPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    // Gerado pelo banco (AUTO_INCREMENT). Não existe setId de propósito.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Preenchida pelo backend no momento da criação (veja aoCriar).
    // updatable = false: depois de gravada, nunca mais muda.
    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "A modalidade do pedido é obrigatória.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModalidadePedido modalidade;

    @NotNull(message = "O status do pedido é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status;

    @NotNull(message = "O canal de origem do pedido é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "canal_origem", nullable = false, length = 30)
    private CanalOrigem canalOrigem;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
    @Column(length = 500)
    private String observacao;

    // Valor em reais, com 2 casas decimais. Começa em zero.
    // Não tem setter genérico: só muda pelo método atualizarValorTotal.
    @NotNull(message = "O valor total é obrigatório.")
    @PositiveOrZero(message = "O valor total não pode ser negativo.")
    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    // Construtor vazio: obrigatório para o JPA funcionar.
    public Pedido() {
    }

    // Construtor com os campos principais.
    // id, dataHora e valorTotal ficam de fora de propósito.
    public Pedido(ModalidadePedido modalidade,
                  StatusPedido status,
                  CanalOrigem canalOrigem,
                  String observacao) {
        this.modalidade = modalidade;
        this.status = status;
        this.canalOrigem = canalOrigem;
        this.observacao = observacao;
    }

    // Executado automaticamente pelo JPA antes do primeiro INSERT.
    @PrePersist
    private void aoCriar() {
        this.dataHora = LocalDateTime.now();
    }

    // Atualiza o valor total do pedido, rejeitando valores inválidos.
    // O cálculo (soma dos subtotais dos itens) não é feito aqui:
    // quem calcula chama este método com o resultado.
    public void atualizarValorTotal(BigDecimal novoTotal) {
        if (novoTotal == null) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser nulo.");
        }
        if (novoTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser negativo.");
        }
        this.valorTotal = novoTotal;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public ModalidadePedido getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadePedido modalidade) {
        this.modalidade = modalidade;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public CanalOrigem getCanalOrigem() {
        return canalOrigem;
    }

    public void setCanalOrigem(CanalOrigem canalOrigem) {
        this.canalOrigem = canalOrigem;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}