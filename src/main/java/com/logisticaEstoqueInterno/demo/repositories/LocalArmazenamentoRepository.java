package com.logisticaEstoqueInterno.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.LocalArmazenamento;

public interface LocalArmazenamentoRepository extends JpaRepository<LocalArmazenamento, Long> {

    // Lista apenas locais ativos — respeita o soft delete (ativo = true)
    // Locais inativos não podem receber novas movimentações (regra AGENTS.md §5)
    List<LocalArmazenamento> findByAtivoTrue();

    // Busca por nome exato — usado no service para verificar duplicidade antes de criar.
    // O Spring Data gera a query automaticamente a partir do nome do método:
    // SELECT * FROM locais_armazenamento WHERE nome = ?
    Optional<LocalArmazenamento> findByNome(String nome);
}
