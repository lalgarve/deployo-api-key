# Contrato de CLI: generate-api-key

Escrito antes da implementação — a implementação segue o contrato.

## Comando: `generate`

**Uso:**
```
deployo-api-key generate --service <nome-do-servico> [--validity-days <dias>]
```

**Argumentos:**

| Argumento | Obrigatório | Descrição |
|---|---|---|
| `--service` | sim | Nome do serviço/cliente que vai usar esta chave. Não pode ser vazio ou só espaço em branco. |
| `--validity-days` | não | Número de dias de validade da chave, a partir de agora. Inteiro positivo (> 0). Se omitido, a chave não tem prazo de validade (indeterminada). |

**Saída (stdout), sucesso — com `--validity-days`:**
```
API key generated for service 'email-service' (expires in 90 days).
This is the only time the plaintext key is shown — store it now:

dak_9f2c1a4e7b3d8f0a1c5e6b7d8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a

```

**Saída (stdout), sucesso — sem `--validity-days`:**
```
API key generated for service 'email-service' (does not expire).
This is the only time the plaintext key is shown — store it now:

dak_9f2c1a4e7b3d8f0a1c5e6b7d8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a

```

**Exit codes:**

| Código | Significado |
|---|---|
| 0 | Sucesso — chave gerada e persistida |
| 1 | Erro de uso: `--service` ausente ou vazio |
| 2 | Erro de configuração: pepper do HMAC não configurado |
| 3 | Erro de persistência: falha ao gravar no banco de dados |

**Erros (stderr):**

| Condição | Mensagem | Exit code |
|---|---|---|
| `--service` ausente | `Error: --service is required.` | 1 |
| `--service` vazio/em branco | `Error: --service must not be blank.` | 1 |
| `--validity-days` inválido (zero, negativo ou não-numérico) | `Error: --validity-days must be a positive integer.` | 1 |
| Pepper do HMAC não configurado | `Error: HMAC pepper is not configured. Set the <VAR> environment variable.` (nome exato da variável definido na implementação) | 2 |
| Falha ao persistir no banco | `Error: could not save the generated key. No key was printed.` | 3 |

Em qualquer caso de erro, nenhuma linha é persistida e nenhuma chave em texto puro é
impressa — a chave só é mostrada depois que a persistência do hash é confirmada com sucesso.
