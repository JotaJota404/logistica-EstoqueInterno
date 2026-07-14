package com.logisticaEstoqueInterno.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logisticaEstoqueInterno.demo.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Busca categoria pelo nome — usado no Service para validar duplicidade
    // antes de lançar uma exceção legível em vez de uma ConstraintViolationException bruta
    Optional<Categoria> findByNome(String nome);
}
