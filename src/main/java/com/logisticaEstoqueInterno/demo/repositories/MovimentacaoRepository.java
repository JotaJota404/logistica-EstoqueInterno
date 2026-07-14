package com.logisticaEstoqueInterno.demo.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.Movimentacao;
import com.logisticaEstoqueInterno.demo.models.TipoMovimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Histórico completo de movimentações de um produto (ordenado por data no Service)
    List<Movimentacao> findByProdutoId(Long produtoId);

    // Filtro por tipo: ENTRADA, SAIDA ou TRANSFERENCIA
    List<Movimentacao> findByTipo(TipoMovimentacao tipo);

    // Filtro por período — base para os relatórios de histórico da Sprint 3
    // "Between" no nome gera: WHERE data_hora >= :inicio AND data_hora <= :fim
    List<Movimentacao> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    // Histórico de movimentações de um produto filtrado por tipo
    List<Movimentacao> findByProdutoIdAndTipo(Long produtoId, TipoMovimentacao tipo);
}
