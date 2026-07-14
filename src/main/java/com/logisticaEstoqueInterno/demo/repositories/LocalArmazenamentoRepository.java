package com.logisticaEstoqueInterno.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.LocalArmazenamento;

public interface LocalArmazenamentoRepository extends JpaRepository<LocalArmazenamento, Long> {

    // Lista apenas locais ativos — respeita o soft delete (ativo = true)
    // Locais inativos não podem receber novas movimentações (regra AGENTS.md §5)
    List<LocalArmazenamento> findByAtivoTrue();
}
