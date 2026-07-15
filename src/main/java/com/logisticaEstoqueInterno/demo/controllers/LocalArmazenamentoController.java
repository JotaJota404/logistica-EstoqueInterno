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
import com.logisticaEstoqueInterno.demo.dtos.LocalArmazenamentoRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.LocalArmazenamentoResponseDTO;
import com.logisticaEstoqueInterno.demo.services.LocalArmazenamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/locais-armazenamento")
public class LocalArmazenamentoController {

    private final LocalArmazenamentoService localArmazenamentoService;

    public LocalArmazenamentoController(LocalArmazenamentoService localArmazenamentoService) {
        this.localArmazenamentoService = localArmazenamentoService;
    }

    // POST /api/locais-armazenamento — 201 Created
    @PostMapping
    public ResponseEntity<LocalArmazenamentoResponseDTO> criar(@Valid @RequestBody LocalArmazenamentoRequestDTO dto) {
        LocalArmazenamentoResponseDTO resposta = localArmazenamentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    // GET /api/locais-armazenamento — 200 OK (apenas ativos)
    @GetMapping
    public ResponseEntity<List<LocalArmazenamentoResponseDTO>> listarAtivos() {
        List<LocalArmazenamentoResponseDTO> lista = localArmazenamentoService.listarAtivos();
        return ResponseEntity.ok(lista);
    }

    // GET /api/locais-armazenamento/{id} — 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<LocalArmazenamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        LocalArmazenamentoResponseDTO resposta = localArmazenamentoService.buscarPorId(id);
        return ResponseEntity.ok(resposta);
    }

    // PUT /api/locais-armazenamento/{id} — 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<LocalArmazenamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocalArmazenamentoRequestDTO dto) {
        LocalArmazenamentoResponseDTO resposta = localArmazenamentoService.atualizar(id, dto);
        return ResponseEntity.ok(resposta);
    }

    // DELETE /api/locais-armazenamento/{id} — 204 No Content (soft delete — marca ativo = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        localArmazenamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
