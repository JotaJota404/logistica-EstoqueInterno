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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logisticaEstoqueInterno.demo.dtos.ProdutoRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.ProdutoResponseDTO;
import com.logisticaEstoqueInterno.demo.services.ProdutoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // POST /api/produtos — 201 Created
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO resposta = produtoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    // GET /api/produtos — 200 OK (apenas ativos)
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarAtivos() {
        List<ProdutoResponseDTO> lista = produtoService.listarAtivos();
        return ResponseEntity.ok(lista);
    }

    // GET /api/produtos/categoria?categoriaId=1 — 200 OK
    // @RequestParam le da query string (?categoriaId=1), diferente do @PathVariable que le da URL (/1)
    @GetMapping("/categoria")
    public ResponseEntity<List<ProdutoResponseDTO>> listarPorCategoria(@RequestParam Long categoriaId) {
        List<ProdutoResponseDTO> lista = produtoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(lista);
    }

    // GET /api/produtos/busca?nome=monitor — 200 OK
    @GetMapping("/busca")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> lista = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(lista);
    }

    // GET /api/produtos/{id} — 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO resposta = produtoService.buscarPorId(id);
        return ResponseEntity.ok(resposta);
    }

    // PUT /api/produtos/{id} — 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO resposta = produtoService.atualizar(id, dto);
        return ResponseEntity.ok(resposta);
    }

    // DELETE /api/produtos/{id} — 204 No Content (soft delete — marca ativo = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        produtoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
