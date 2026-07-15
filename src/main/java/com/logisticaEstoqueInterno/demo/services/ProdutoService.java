package com.logisticaEstoqueInterno.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.logisticaEstoqueInterno.demo.dtos.CategoriaResponseDTO;
import com.logisticaEstoqueInterno.demo.dtos.ProdutoRequestDTO;
import com.logisticaEstoqueInterno.demo.dtos.ProdutoResponseDTO;
import com.logisticaEstoqueInterno.demo.models.Categoria;
import com.logisticaEstoqueInterno.demo.models.Produto;
import com.logisticaEstoqueInterno.demo.repositories.CategoriaRepository;
import com.logisticaEstoqueInterno.demo.repositories.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    //
    // Criar
    //
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        // SKU e o campo unico do Produto, nao o nome.
        // findBySku() retorna Optional<Produto> â€” mesmo padrao do findByNome() no
        // LocalArmazenamento.
        if (produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new RuntimeException("Ja existe um produto com o SKU: " + dto.sku());
        }

        // O DTO traz apenas categoriaId (Long). Precisamos do OBJETO Categoria
        // para passar ao construtor do Produto. Por isso temos o categoriaRepository
        // aqui.
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada: ID " + dto.categoriaId()));

        // Construtor do Produto exige 6 parametros â€” todos os campos nao-nulos exceto
        // ativo e timestamps.
        Produto produto = new Produto(dto.nome(), dto.descricao(), dto.sku(),
                dto.unidadeMedida(), dto.estoqueMinimo(), categoria);

        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);
    }

    //
    // Listar ativos
    // Listar por Categoria
    // Buscar por nome (pesquisa parcial, ignora maiusculas/minusculas)
    // Ex: buscar "mon" encontra "Monitor Dell" e "Monitor LG"
    //
    public List<ProdutoResponseDTO> listarAtivos() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // GET /api/produtos/{id}
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: ID " + id));
        return toResponseDTO(produto);
    }

    //
    // Atualizar
    //
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: ID " + id));

        // Categoria pode ser trocada na atualizacao â€” buscamos o novo objeto pelo ID do DTO
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada: ID " + dto.categoriaId()));

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setSku(dto.sku());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setCategoria(categoria);

        Produto atualizado = produtoRepository.save(produto);
        return toResponseDTO(atualizado);
    }

    //
    // Desativar (Soft Delete) â€” nao deleta, apenas marca ativo = false
    //
    public void desativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: ID " + id));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    //
    // Metodo auxiliar privado
    // Diferenca em relacao aos outros services: ProdutoResponseDTO exige um
    // CategoriaResponseDTO aninhado â€” entao construimos ele aqui antes de retornar.
    //
    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.getCategoria().getDescricao());

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getSku(),
                produto.getUnidadeMedida(),
                produto.getEstoqueMinimo(),
                produto.getAtivo(),
                categoriaDTO,
                produto.getDataCriacao(),
                produto.getDataAtualizacao());
    }
}
