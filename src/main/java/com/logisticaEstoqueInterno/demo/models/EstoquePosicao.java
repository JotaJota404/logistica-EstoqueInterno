package com.logisticaEstoqueInterno.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "estoque_posicao", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "produto_id", "local_id" })
})
public class EstoquePosicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private LocalArmazenamento localArmazenamento;

    @Column(nullable = false)
    private Integer quantidadeAtual = 0;

    @Column(nullable = false)
    private LocalDateTime ultimaAtualizacao;

    public EstoquePosicao() {
    }

    public EstoquePosicao(Produto produto, LocalArmazenamento localArmazenamento, Integer quantidadeAtual) {
        this.produto = produto;
        this.localArmazenamento = localArmazenamento;
        this.quantidadeAtual = quantidadeAtual;
    }

    @PrePersist
    @PreUpdate
    private void atualizarTimestamp() {
        this.ultimaAtualizacao = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public LocalArmazenamento getLocalArmazenamento() {
        return localArmazenamento;
    }

    public void setLocalArmazenamento(LocalArmazenamento localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }
}