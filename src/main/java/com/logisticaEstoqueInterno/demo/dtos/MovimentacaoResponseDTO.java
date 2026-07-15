package com.logisticaEstoqueInterno.demo.dtos;

import java.time.LocalDateTime;
import com.logisticaEstoqueInterno.demo.models.TipoMovimentacao;

public record MovimentacaoResponseDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        String localOrigemNome,
        String localDestinoNome,
        TipoMovimentacao tipo,
        Integer quantidade,
        String motivo,
        String responsavel,
        LocalDateTime dataHora
) {}
