# Tasks: generate-api-key

| ID | Descrição | Depende de | Paralelizável | Issue |
|---|---|---|---|---|
| T000 | Resolver as decisões em aberto de `plan.md` (motor de banco, mecanismo de acesso, ferramenta de migration) | — | | #2 |
| T001 | Criar schema/migration da tabela `api_keys` (`data-model.md`) | T000 | | #2 |
| T002 | Implementar geração de chave aleatória com prefixo `dak_` (32 bytes de entropia, base64url) | — | [P] | #2 |
| T003 | Implementar hashing HMAC-SHA256 lendo o pepper de variável de ambiente, com erro claro se ausente | — | [P] | #2 |
| T004 | Implementar persistência: inserir `service_name` + `key_hash` + `created_at` em `api_keys` | T001 | | #2 |
| T005 | Implementar o comando `generate` da CLI (parsing de `--service`, orquestração geração → hash → persistência → impressão única da chave), seguindo `contracts/cli-commands.md` | T002, T003, T004 | | #2 |
| T006 | Testes: geração bem-sucedida grava linha e imprime a chave uma única vez; `--service` ausente/vazio falha com exit code 1 sem persistir; pepper ausente falha com exit code 2 sem persistir nem imprimir; falha de persistência retorna exit code 3 | T005 | | #2 |
| T007 | Documentar o procedimento operacional de backup do pepper do HMAC (README ou runbook — fora do código) | — | [P] | #2 |

- **[P]** marca tarefas que não dependem umas das outras e podem ser feitas em paralelo.
- T000 bloqueia T001 porque o schema depende do motor de banco e do mecanismo de acesso,
  ainda marcados "em aberto" em `plan.md` — vira um commit `decision:` quando resolvida.
- Marcar o ID como concluído (`~~T00N~~` ou checkbox `[x]`) quando o commit que a resolve for
  mesclado.
