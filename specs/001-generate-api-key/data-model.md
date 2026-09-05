# Data model: generate-api-key

## Entidades

### ApiKey (tabela `api_keys`)

| Campo | Tipo | Obrigatório | Validação |
|---|---|---|---|
| `id` | BIGSERIAL | sim | gerado automaticamente na inserção |
| `service_name` | VARCHAR(255) | sim | não vazio; identifica o serviço/cliente dono da chave |
| `key_hash` | VARCHAR(255) | sim | HMAC-SHA256 da chave em texto puro, em hexadecimal; único (índice `UNIQUE`) |
| `created_at` | TIMESTAMP (UTC) | sim | gerado automaticamente na inserção |
| `expires_at` | TIMESTAMP (UTC) | não | nulo = validade indeterminada; quando definido, igual a `created_at` + N dias (`--validity-days`) |

`BIGSERIAL`/`VARCHAR(255)` seguem a mesma convenção de tipos do `jogo-acoes` (ver
`app/src/main/resources/db/migration/V1__init_schema.sql`), agora que motor de banco e
mecanismo de acesso estão resolvidos em `plan.md`. Migration via Flyway, em dois diretórios
paralelos (`db/migration` para PostgreSQL, `db/migration-h2` para o perfil `sandbox`) — mesmo
padrão do `jogo-acoes`, necessário porque o schema roda contra motores diferentes conforme o
perfil ativo. Mapeamento por Hibernate/Spring Data JPA chega na T005.

## Relacionamentos

Nenhum — tabela isolada nesta feature. Uma relação com uma futura tabela de "serviços
cadastrados" pode fazer sentido quando `service_name` deixar de aceitar qualquer string (ver
decisão em aberto em `spec.md`), mas isso não é modelado agora.

## Invariantes

- `key_hash` é único: duas chaves nunca colidem no hash armazenado.
- A chave em texto puro nunca é persistida em nenhuma coluna desta ou de qualquer outra
  tabela.
- `service_name` nunca é vazio ou só espaço em branco (validado antes da persistência, não
  só no banco).
- `expires_at`, quando não nulo, é sempre posterior a `created_at` — garantido por
  construção, já que é sempre calculado como `created_at` + N dias com N > 0 (nunca definido
  diretamente pelo operador).
