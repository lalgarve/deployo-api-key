# Diário de desenvolvimento

Resumo diário do que foi feito, com os commits e Issues envolvidos. Ver "Diário de
desenvolvimento" em `memory/constitution.md` para o formato. Não é documentação de produto —
não deve ser publicada junto com o restante de `docs/`.

## 2026-09-04

**Resumo:** adotada a estrutura de Spec-Driven Development (SDD) para o projeto —
`memory/constitution.md` (convenções, adaptado do `desenvolvimento.md` do projeto
`jogo-acoes`), `templates/` para novas features, `specs/` para as specs vivas, e este
diário. Rastreamento de trabalho passa a usar Issues do GitHub, espelhando `tasks.md` de
cada feature. README atualizado com o contexto do projeto (parte do `jogo-acoes`,
API-KEY como padrão de autenticação entre APIs internas, admin UI fora de escopo por
enquanto), o papel deste repositório como exemplo de SDD para a disciplina, e a
divulgação de uso de ferramentas de IA.

**Commits:**
- `9a58b33` docs: adopt Spec-Driven Development structure
- `062a18e` docs: record commit hash in today's diary entry
- `868caac` docs: explain project context and disclose AI tool usage in README
- `805b7ea` docs: add Issues to the language convention table

**Issues:** —

## 2026-09-05

**Resumo:** primeira feature real do projeto — `specs/001-generate-api-key/` (spec, plan,
data-model, contrato de CLI e tasks) para o comando `generate`, que emite uma API-KEY para um
serviço consumidor e persiste só o hash HMAC-SHA256. Três decisões técnicas ficaram em
aberto em `plan.md` (motor de banco, mecanismo de acesso, ferramenta de migration), bloqueando
a tarefa T001. Criada a Issue-épico #2 no GitHub, espelhando `tasks.md`.

As três decisões em aberto foram resolvidas no mesmo dia: Spring Data JPA/Hibernate,
PostgreSQL (`docker`/CI) + H2 (`sandbox`) e Flyway — mesmo padrão do `jogo-acoes`,
priorizando consistência entre os dois projetos do portfólio sobre otimizar esta CLI
isoladamente. Adicionado também um prazo de validade opcional à chave (`--validity-days`,
inteiro positivo; se omitido, a chave não expira) — `expires_at` na tabela, verificação de
expiração em si continua fora de escopo (fica com a futura biblioteca de leitura).

**Commits:**
- `6ddc76a` feat: add spec, plan and tasks for generate-api-key
- `c2c9351` decision: use Spring Data JPA/Hibernate, PostgreSQL+H2 and Flyway for generate-api-key
- `df28ebe` feat: add optional key validity period to generate-api-key

**Issues:** #2 aberta (épico da feature 001, permanece aberta até T001-T008 serem implementadas)
