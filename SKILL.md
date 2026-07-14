---
name: code-review
description: Revisa mudanças de código Java/Spring Boot deste projeto (SGE) buscando bugs, acoplamento ruim, violação da arquitetura em camadas e falta de validação. Use quando o usuário pedir para "revisar", "fazer code review", ou "atuar como Tech Lead" sobre um arquivo ou classe.
---

# Checklist de Code Review — SGE

Ao revisar uma classe ou um diff, siga esta ordem e aponte problemas — **não
corrija automaticamente**, a menos que o usuário peça explicitamente para corrigir.
O objetivo é que o próprio usuário entenda o problema e corrija com as próprias
mãos.

## 1. Arquitetura em camadas

- A classe está na pasta correta (models / repositories / dtos / services /
  controllers / exceptions / config)?
- Um `Controller` contém alguma regra de negócio (`if`/`else` de validação) que
  deveria estar no `Service`?
- Um `Service` está fazendo query manual que deveria estar no `Repository`?
- Um `Repository` tem lógica além de métodos de acesso a dados?

## 2. Modelagem JPA

- Relacionamentos (`@ManyToOne`, `@OneToMany`) fazem sentido para o domínio?
- Campos obrigatórios têm `nullable = false`?
- Enums estão com `@Enumerated(EnumType.STRING)` (nunca `ORDINAL`)?
- Chaves únicas/compostas usam `@UniqueConstraint` em vez de validação manual
  redundante?
- Timestamps automáticos usam `@PrePersist`/`@PreUpdate`, não são setados na mão
  no Service?

## 3. Validação e DTOs

- Todo DTO de request tem Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`,
  etc.)?
- O Controller usa `@Valid` no `@RequestBody`?
- O DTO está exposto sem vazar a entidade JPA diretamente para a API?

## 4. Regras de negócio específicas do domínio

- Saída ou transferência valida saldo suficiente antes de decrementar?
- Atualização de `EstoquePosicao` e criação de `Movimentacao` acontecem na mesma
  transação (`@Transactional`)?
- Soft delete (`ativo = false`) é respeitado em vez de deletar registros com
  histórico?

## 5. Tratamento de erros

- Exceções de negócio são customizadas (não `RuntimeException` genérica)?
- Existe um cenário de erro que não está sendo tratado (ex: produto inexistente,
  local inativo)?

## 6. Acoplamento e legibilidade

- A classe depende de coisas que não deveria conhecer (ex: Service conhecendo
  detalhe de HTTP)?
- Métodos muito longos que deveriam ser quebrados?
- Nomes de variáveis/métodos estão claros e em português OU inglês de forma
  consistente com o resto do projeto?

## 7. Testes (quando aplicável)

- Existe teste para o caminho de erro (ex: saldo insuficiente), não só o caminho
  feliz?
- O teste segue o padrão Given-When-Then comentado?

---

## Formato da resposta de revisão

Ao final, liste os problemas encontrados em ordem de severidade (crítico → menor),
cada um com:

1. **O que está errado** (uma frase)
2. **Por que é um problema** (o risco real — bug, acoplamento, dado inconsistente)
3. **Pergunta orientadora** para o usuário corrigir sozinho (não a correção pronta)

Exemplo:
> **Crítico** — `MovimentacaoService.registrarSaida()` não está anotado com
> `@Transactional`. Se a atualização do `EstoquePosicao` falhar depois de já ter
> salvo a `Movimentacao`, o banco fica inconsistente. Que anotação do Spring
> garante que as duas operações aconteçam juntas ou nenhuma aconteça?
