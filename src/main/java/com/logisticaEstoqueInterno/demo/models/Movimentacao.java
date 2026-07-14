package com.logisticaEstoqueInterno.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"produto", "localOrigem", "localDestino"})
@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "local_origem_id") // Permite null para ENTRADA
    private LocalArmazenamento localOrigem;

    @ManyToOne
    @JoinColumn(name = "local_destino_id") // Permite null para SAIDA
    private LocalArmazenamento localDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 500)
    private String motivo;

    @Column(nullable = false, length = 100)
    private String responsavel;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHora;

    public Movimentacao(Produto produto, LocalArmazenamento localOrigem, LocalArmazenamento localDestino,
            TipoMovimentacao tipo, Integer quantidade, String motivo, String responsavel) {
        this.produto = produto;
        this.localOrigem = localOrigem;
        this.localDestino = localDestino;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.responsavel = responsavel;
    }

    @PrePersist
    protected void antesDeSalvar() {
        this.dataHora = LocalDateTime.now(ZoneOffset.UTC);
    }

    // Métodos utilitários de negócio
    public boolean isEntrada() {
        return this.tipo == TipoMovimentacao.ENTRADA;
    }

    public boolean isSaida() {
        return this.tipo == TipoMovimentacao.SAIDA;
    }

    public boolean isTransferencia() {
        return this.tipo == TipoMovimentacao.TRANSFERENCIA;
    }
}
