package com.logisticaEstoqueInterno.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca produto pelo SKU único — usado para evitar duplicidade de SKU
    Optional<Produto> findBySku(String sku);

    // Lista apenas produtos ativos — respeita o soft delete (ativo = true)
    List<Produto> findByAtivoTrue();

    // Lista produtos de uma categoria específica
    List<Produto> findByCategoriaId(Long categoriaId);
}
