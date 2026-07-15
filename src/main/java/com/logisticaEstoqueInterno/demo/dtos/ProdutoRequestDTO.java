package com.logisticaEstoqueInterno.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotBlank(message = "SKU é obrigatório")
        @Size(max = 50, message = "SKU deve ter no máximo 50 caracteres")
        String sku,

        @NotBlank(message = "Unidade de medida é obrigatória")
        @Size(max = 20, message = "Unidade de medida deve ter no máximo 20 caracteres")
        String unidadeMedida,

        // @PositiveOrZero: valor 0 é válido e significa "sem mínimo definido"
        @NotNull(message = "Estoque mínimo é obrigatório")
        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer estoqueMinimo,

        @NotNull(message = "Categoria é obrigatória")
        Long categoriaId

) {}
