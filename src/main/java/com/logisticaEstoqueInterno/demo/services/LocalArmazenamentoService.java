package com.logisticaEstoqueInterno.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.logisticaEstoqueInterno.demo.dtos.LocalArmazenamentoRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.LocalArmazenamentoResponseDTO;
import com.logisticaEstoqueInterno.demo.models.LocalArmazenamento;
import com.logisticaEstoqueInterno.demo.repositories.LocalArmazenamentoRepository;

// @Service: marca esta classe como um componente de servico do Spring.
// O Spring a detecta automaticamente e a registra no contexto para ser
// injetada onde for necessario (ex: no Controller).
@Service
public class LocalArmazenamentoService {

    // Injecao via construtor (padrao recomendado no Spring Boot).
    // O campo e final: garante que o repository nunca seja null apos a construcao.
    private final LocalArmazenamentoRepository localArmazenamentoRepository;

    public LocalArmazenamentoService(LocalArmazenamentoRepository localArmazenamentoRepository) {
        this.localArmazenamentoRepository = localArmazenamentoRepository;
    }

    // -------------------------------------------------------------------
    // CRIAR
    // -------------------------------------------------------------------
    public LocalArmazenamentoResponseDTO criar(LocalArmazenamentoRequestDTO dto) {
        if (localArmazenamentoRepository.findByNome(dto.nome()).isPresent()) {
            throw new RuntimeException("Ja existe um local de armazenamento com o nome: " + dto.nome());
        }
        LocalArmazenamento local = new LocalArmazenamento(dto.nome(), dto.descricao());
        LocalArmazenamento salvo = localArmazenamentoRepository.save(local);

        return toResponseDTO(salvo);
    }

    // -------------------------------------------------------------------
    // LISTAR ATIVOS
    // -------------------------------------------------------------------
    public List<LocalArmazenamentoResponseDTO> listarAtivos() {
        List<LocalArmazenamento> locais = localArmazenamentoRepository.findByAtivoTrue();
        return locais.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // -------------------------------------------------------------------
    // BUSCAR POR ID
    // -------------------------------------------------------------------
    public LocalArmazenamentoResponseDTO buscarPorId(Long id) {
        LocalArmazenamento local = localArmazenamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local de armazenamento nao encontrado: ID " + id));

        return toResponseDTO(local);
    }

    // -------------------------------------------------------------------
    // ATUALIZAR
    // -------------------------------------------------------------------
    public LocalArmazenamentoResponseDTO atualizar(Long id, LocalArmazenamentoRequestDTO dto) {
        LocalArmazenamento local = localArmazenamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local de armazenamento nao encontrado: ID " + id));

        local.setNome(dto.nome());
        local.setDescricao(dto.descricao());

        LocalArmazenamento atualizado = localArmazenamentoRepository.save(local);
        return toResponseDTO(atualizado);
    }

    // -------------------------------------------------------------------
    // DESATIVAR (Soft Delete)
    // -------------------------------------------------------------------
    public void desativar(Long id) {
        LocalArmazenamento local = localArmazenamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local de armazenamento nao encontrado: ID " + id));

        local.setAtivo(false);
        localArmazenamentoRepository.save(local);
    }

    // -------------------------------------------------------------------
    // METODO AUXILIAR PRIVADO
    // -------------------------------------------------------------------
    private LocalArmazenamentoResponseDTO toResponseDTO(LocalArmazenamento local) {
        return new LocalArmazenamentoResponseDTO(
                local.getId(),
                local.getNome(),
                local.getDescricao(),
                local.getAtivo());
    }
}