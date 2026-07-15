package com.logisticaEstoqueInterno.demo.dtos;

import java.time.LocalDateTime;

public record EstoquePosicaoResponseDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        String produtoSku,
        Long localId,
        String localNome,
        Integer quantidadeAtual,
        LocalDateTime ultimaAtualizacao
) {}
