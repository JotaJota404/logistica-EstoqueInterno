package com.logisticaEstoqueInterno.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.logisticaEstoqueInterno.demo.dtos.CategoriaRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.CategoriaResponseDTO;
import com.logisticaEstoqueInterno.demo.models.Categoria;
import com.logisticaEstoqueInterno.demo.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.findByNome(dto.nome()).isPresent()) {
            throw new RuntimeException("Já existe uma categoria com o nome:" + dto.nome());
        }
        Categoria categoria = new Categoria(dto.nome(), dto.descricao());

        Categoria salva = categoriaRepository.save(categoria);

        return toResponseDTO(salva);
    }

    public List<CategoriaResponseDTO> listarTodas() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada por ID: " + id));
        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada por ID: " + id));

        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());

        Categoria atualizada = categoriaRepository.save(categoria);
        return toResponseDTO(atualizada);
    }

    public void deletar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada por ID: " + id));

        categoriaRepository.delete(categoria);
    }

    // metodos auxiliares
    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao());
    }
}
