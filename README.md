# 📦 SGE — Sistema de Gestão de Estoque Interno

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/H2_Database-Dev-blue?style=for-the-badge&logo=databricks&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-Prod-336791?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
</p>

## 📋 Sobre o Projeto

O **SGE** é uma API RESTful de gestão de estoque interno construída com **Java 17+ e Spring Boot 3+**. O sistema vai além de um CRUD simples — ele modela o ciclo de vida completo do estoque: desde a **entrada de mercadorias**, passando pela **movimentação entre setores/locais de armazenamento**, até a **saída**, mantendo **rastreabilidade completa** de cada operação.

> **Objetivo:** Projeto desenvolvido para aprendizado e portfólio, simulando um ambiente corporativo real com boas práticas de engenharia de software, versionamento com Git Flow e desenvolvimento ágil por Sprints.

---

## 🎯 O que este projeto resolve?

Em empresas reais, controlar estoque não é apenas saber "quantos itens tem". É preciso:

- 📍 Saber **onde** cada produto está armazenado (Setor A, Setor B, Depósito)
- 🔄 Rastrear **movimentações** — quem tirou, quando, e por quê
- 🚫 **Bloquear saídas** quando o estoque é insuficiente
- ⚠️ **Alertar** quando produtos estão abaixo do estoque mínimo
- 📊 Gerar **relatórios** de posição de estoque e movimentações por período

---

## 🏗️ Arquitetura

O projeto segue o padrão **Arquitetura em Camadas (Layered Architecture)**:

```
Cliente HTTP → Controller → Service → Repository → Banco de Dados
                  ↕              ↕
                 DTO          Entity/Model
```

### Estrutura de Pacotes

```
com.logisticaEstoqueInterno.demo
├── config/         → Configurações globais (CORS, Swagger)
├── controllers/    → Endpoints REST (@RestController)
├── dtos/           → Objetos de transferência (entrada/saída da API)
├── exceptions/     → Tratamento global de erros (@ControllerAdvice)
├── models/         → Entidades JPA (@Entity)
├── repositories/   → Interfaces de acesso ao banco (JpaRepository)
└── services/       → Regras de negócio (@Service)
```

---

## 🧩 Entidades do Sistema

| Entidade | Descrição |
|---|---|
| **Produto** | Item do estoque com SKU, categoria, unidade de medida e estoque mínimo |
| **Categoria** | Classificação dos produtos (ex: Eletrônicos, Escritório) |
| **LocalArmazenamento** | Locais físicos onde produtos são guardados (Setor A, Depósito) |
| **EstoquePosicao** | Saldo atual de um produto em um local específico |
| **Movimentacao** | Registro de cada entrada, saída ou transferência (audit trail) |

### Relacionamentos

```
Categoria (1) ←——— (N) Produto (1) ←——— (N) EstoquePosicao (N) ———→ (1) LocalArmazenamento
                            |
                            ↓
                    Movimentacao (N)
                    (origem / destino → LocalArmazenamento)
```

---

## 🛣️ Endpoints da API

### Produtos
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/produtos` | Cadastrar produto |
| `GET` | `/api/produtos` | Listar todos (com filtros) |
| `GET` | `/api/produtos/{id}` | Buscar por ID |
| `PUT` | `/api/produtos/{id}` | Atualizar produto |
| `DELETE` | `/api/produtos/{id}` | Desativar produto (soft delete) |

### Categorias
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/categorias` | Criar categoria |
| `GET` | `/api/categorias` | Listar categorias |

### Locais de Armazenamento
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/locais` | Criar local |
| `GET` | `/api/locais` | Listar locais |
| `GET` | `/api/locais/{id}/estoque` | Ver estoque de um local |

### Movimentações
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/movimentacoes/entrada` | Registrar entrada de produto |
| `POST` | `/api/movimentacoes/saida` | Registrar saída de produto |
| `POST` | `/api/movimentacoes/transferencia` | Transferir entre locais |
| `GET` | `/api/movimentacoes` | Histórico (filtro por data/produto) |

### Relatórios
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/relatorios/estoque-baixo` | Produtos abaixo do mínimo |
| `GET` | `/api/relatorios/posicao-estoque` | Saldo atual por produto/local |

---

## 🚀 Organização por Sprints

| Sprint | Foco | Status |
|---|---|---|
| **Sprint 0** | Setup do projeto, configuração do banco H2, estrutura de pacotes | ✅ Concluída |
| **Sprint 1** | CRUD de Produto, Categoria e LocalArmazenamento | 🔲 Pendente |
| **Sprint 2** | Movimentações (entrada, saída, transferência) + regras de bloqueio | 🔲 Pendente |
| **Sprint 3** | Relatórios, tratamento de exceções global, testes unitários | 🔲 Pendente |

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.x | Framework backend |
| Spring Data JPA | — | Persistência de dados |
| H2 Database | — | Banco em memória (desenvolvimento) |
| PostgreSQL | — | Banco relacional (produção) |
| Maven | — | Gerenciamento de dependências e build |
| JUnit 5 | — | Testes unitários e de integração |
| Git + GitHub | — | Versionamento com Git Flow |

---

## ⚙️ Como Rodar Localmente

### Pré-requisitos
- Java 21+
- Maven 3.8+

### Executar

```bash
# Clonar o repositório
git clone https://github.com/JotaJota404/logistica-EstoqueInterno.git

# Entrar na pasta
cd logistica-EstoqueInterno

# Rodar a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

O console do H2 estará em `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:estoquedb`
- **Username:** `sa`
- **Password:** `password`

---

## 🌿 Git Flow

```
main ──────────────────────────────────── (produção)
  └── develop ─────────────────────────── (integração)
        ├── feature/criar-entidade-produto
        ├── feature/movimentacoes
        └── fix/corrigir-bug-estoque
```

- **`main`** → Código de produção (só recebe merges da `develop`)
- **`develop`** → Branch de integração
- **`feature/*`** → Branches de trabalho para cada tarefa

---

## 📄 Licença

Este projeto é de uso educacional e faz parte do meu portfólio de desenvolvimento backend.

---

<p align="center">
  Desenvolvido com ☕ e dedicação por <a href="https://github.com/JotaJota404">JotaJota404</a>
</p>
