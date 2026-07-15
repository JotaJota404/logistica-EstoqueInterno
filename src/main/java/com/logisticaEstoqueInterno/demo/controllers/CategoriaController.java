package com.logisticaEstoqueInterno.demo.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.logisticaEstoqueInterno.demo.dtos.CategoriaRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.CategoriaResponseDTO;
import com.logisticaEstoqueInterno.demo.services.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // POST /api/categorias — 201 Created
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO resposta = categoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    // GET /api/categorias — 200 OK
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        List<CategoriaResponseDTO> lista = categoriaService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    // GET /api/categorias/{id} — 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        CategoriaResponseDTO resposta = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(resposta);
    }

    // PUT /api/categorias/{id} — 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO resposta = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(resposta);
    }

    // DELETE /api/categorias/{id} — 204 No Content (hard delete — Categoria nao tem historico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
