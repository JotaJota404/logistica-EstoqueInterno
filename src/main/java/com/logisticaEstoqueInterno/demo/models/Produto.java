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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "categoria")
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 20)
    private String unidadeMedida;

    @Column(nullable = false)
    private Integer estoqueMinimo;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    @PreUpdate
    private void atualizarTimestamps() {
        LocalDateTime agora = LocalDateTime.now(ZoneOffset.UTC);
        if (this.dataCriacao == null) {
            this.dataCriacao = agora;
        }
        this.dataAtualizacao = agora;
    }

    public Produto(String nome, String descricao, String sku, String unidadeMedida,
            Integer estoqueMinimo, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.sku = sku;
        this.unidadeMedida = unidadeMedida;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
    }
}
