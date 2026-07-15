package com.logisticaEstoqueInterno.demo.dtos;

import java.time.LocalDateTime;

public record ProdutoResponseDTO(

        Long id,
        String nome,
        String descricao,
        String sku,
        String unidadeMedida,
        Integer estoqueMinimo,
        Boolean ativo,

        // Categoria aninhada: o cliente recebe id + nome sem precisar de uma segunda chamada
        CategoriaResponseDTO categoria,

        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao

) {}
