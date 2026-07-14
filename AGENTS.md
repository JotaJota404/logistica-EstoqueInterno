# AGENTS.md — SGE (Sistema de Gestão de Estoque)

> Este arquivo define como qualquer agente de IA deve trabalhar neste repositório.
> O objetivo não é gerar código completo sozinho — é seguir estas convenções à risca
> e sempre gerar um plano antes de codar mudanças de lógica de negócio.

---

## 1. Sobre o projeto

Sistema de gestão de estoque (SGE) em Spring Boot 3 / Java 21, com banco H2 (dev) e
migração planejada para Postgres. Domínio: produtos, categorias, locais de
armazenamento, posições de estoque e movimentações (entrada, saída, transferência).

Pacote raiz: `com.logisticaEstoqueInterno.demo`

---

## 2. A Bússola das 7 Pastas (regra imutável)

Nunca crie um arquivo fora do lugar certo. Se não tiver certeza de onde algo vai,
pare e pergunte antes de criar.

| Pasta | O que entra | O que NUNCA entra |
|---|---|---|
| `models` | Classes `@Entity`, `@Table`, enums | Lógica de negócio, chamadas a repository |
| `repositories` | Interfaces que estendem `JpaRepository` | Qualquer `if`, cálculo ou validação |
| `dtos` | `record` do Java 21 para request/response | Anotações `@Entity` |
| `services` | Classes `@Service` — regra de negócio, validações, cálculos | Anotações `@RestController`, chamadas HTTP |
| `controllers` | Classes `@RestController` — só mapeiam rota, recebem DTO, chamam Service | Lógica de negócio (if/else de regra) |
| `exceptions` | Exceções customizadas + `GlobalExceptionHandler` | — |
| `config` | Configurações do Spring (Swagger, CORS, Security) que não são regra de negócio | — |

---

## 3. Convenções de código

- **Java 21**: usar `record` para todos os DTOs. Nunca criar DTO como classe com getters/setters manuais.
- **Entidades**: getters/setters explícitos (sem Lombok, a menos que combinado explicitamente).
- **Soft delete**: entidades que podem ter histórico vinculado (ex: `Produto`) usam campo `ativo` (Boolean). Nunca implementar hard delete para essas entidades.
- **Timestamps automáticos**: quando uma entidade precisa de campo de última atualização, usar `@PrePersist` + `@PreUpdate` no mesmo método privado dentro da própria entidade — não setar o timestamp manualmente no Service.
- **Enums**: sempre salvos como `@Enumerated(EnumType.STRING)`, nunca `ORDINAL`.
- **Chaves compostas/únicas**: usar `@UniqueConstraint` na `@Table`, não validação manual de duplicidade quando o banco já pode garantir.
- **Validação de entrada**: todo DTO de request deve ter Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`, etc.) e todo Controller deve usar `@Valid` no `@RequestBody`.
- **Exceções de negócio**: nunca lançar `RuntimeException` genérica. Criar exceção customizada específica (ex: `SaldoInsuficienteException`) e tratá-la no `GlobalExceptionHandler`.

---

## 4. Fluxo obrigatório para qualquer tarefa nova

1. **Nunca gerar código de lógica de negócio direto em Fast mode.** Para qualquer
   Service, Controller ou regra de validação, usar **Planning mode** e apresentar
   o plano antes de escrever qualquer linha.
2. **Uma tarefa = uma classe ou um método por vez.** Não implementar uma Sprint
   inteira em uma única execução. Se o pedido cobrir múltiplas classes, dividir
   em sub-tarefas e apresentar cada uma separadamente para aprovação.
3. **Sempre explicar o "porquê"** de decisões não triviais no plano (ex: por que
   usar `@ManyToOne` em vez de `@OneToMany` num relacionamento, por que um campo é
   `nullable = false`).
4. **Não escrever testes automaticamente sem pedido explícito.** Quando pedido,
   seguir estritamente o padrão Given-When-Then (seção 6).
5. **Não instalar dependências novas sem avisar antes** e explicar o motivo.
6. **Nunca alterar `ddl-auto` de `update` para outra coisa, nem tocar em migrations
   do Flyway, sem confirmação explícita** — schema de banco é sensível.

---

## 5. Regras de negócio centrais (não violar)

- Uma **SAÍDA** ou **TRANSFERÊNCIA** só pode ser registrada se `quantidadeAtual >=
  quantidade solicitada` no `EstoquePosicao` de origem. Caso contrário, lançar
  `SaldoInsuficienteException`.
- Toda movimentação (`ENTRADA`, `SAIDA`, `TRANSFERENCIA`) deve gerar um registro em
  `Movimentacao` **e** atualizar o(s) `EstoquePosicao` correspondente(s) na mesma
  transação (`@Transactional`).
- Uma `TRANSFERENCIA` nunca pode ter `localOrigem == localDestino`.
- Produtos e Locais de Armazenamento inativos (`ativo = false`) não podem receber
  novas movimentações.

---

## 6. Padrão de testes — Given-When-Then

```java
@Test
void deveLancarExcecaoQuandoSaldoForInsuficiente() {
    // GIVEN (Dado o cenário inicial)
    // Ex: produto com saldo 10 no mock do repository

    // WHEN (Quando a ação ocorrer)
    // Ex: chamar registrarSaida() pedindo 15

    // THEN (Então verifique o resultado)
    // Ex: verificar que SaldoInsuficienteException foi lançada
}
```

Foco de testes: camada `Service`. Não gastar tempo testando `Controller` ou
`Repository` nesta fase do projeto, a menos que pedido explicitamente.

---

## 7. O que o agente NUNCA deve fazer sem perguntar antes

- Criar arquivo fora da estrutura de 7 pastas.
- Implementar mais de uma Sprint (ou mais de ~1 classe completa) numa única
  execução sem parar para revisão.
- Aceitar ambiguidade de regra de negócio e "decidir sozinho" — se uma regra não
  estiver clara (ex: o que fazer se quantidade solicitada for exatamente igual ao
  saldo), perguntar ao usuário antes de assumir um comportamento.
- Remover ou reescrever testes existentes sem justificar o motivo no plano.
- Adicionar autenticação/segurança, alterar `application.yaml` de produção, ou
  mexer em configuração de deploy sem confirmação explícita.

---

## 8. Contexto de aprendizado (importante)

O responsável por este repositório está aprendendo Spring Boot ativamente e usando
IA como ferramenta de apoio, não como substituto do próprio raciocínio. Por isso:

- Priorize **explicar o plano e o porquê** antes de gerar código.
- Prefira gerar **menos código por vez**, mesmo que isso signifique mais idas e
  vindas — o objetivo aqui é entendimento, não apenas velocidade.
- Se o usuário pedir para "explicar linha por linha", faça isso de forma didática,
  citando a anotação/conceito do Spring ou Java envolvido.
