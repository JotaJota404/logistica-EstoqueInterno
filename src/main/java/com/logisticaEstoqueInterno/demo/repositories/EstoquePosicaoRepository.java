package com.logisticaEstoqueInterno.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.EstoquePosicao;

public interface EstoquePosicaoRepository extends JpaRepository<EstoquePosicao, Long> {

    // Query mais crítica do sistema: localiza o saldo exato de um produto em um local.
    // Usada pelo MovimentacaoService antes de aprovar qualquer SAÍDA ou TRANSFERÊNCIA.
    // Retorna Optional porque a posição pode não existir (ex: primeira ENTRADA naquele local).
    Optional<EstoquePosicao> findByProdutoIdAndLocalArmazenamentoId(Long produtoId, Long localArmazenamentoId);

    // Todo o estoque de um produto distribuído por todos os locais de armazenamento
    List<EstoquePosicao> findByProdutoId(Long produtoId);

    // Todo o estoque de um local específico (útil para relatório de posição de estoque)
    List<EstoquePosicao> findByLocalArmazenamentoId(Long localArmazenamentoId);
}
