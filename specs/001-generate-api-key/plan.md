# Plan: generate-api-key

Traduz `spec.md` em decisões técnicas. Valida contra `memory/constitution.md`.

## Contexto técnico

Aplicação de linha de comando em Java, de execução curta (roda, grava uma linha, sai) — não
um serviço de longa duração. Escreve num banco de dados relacional compartilhado com o(s)
serviço(s) consumidor(es), que só têm acesso de leitura (`SELECT`) a essa tabela.

## Decisões de arquitetura

| Pergunta | Decisão | Status | Raciocínio |
|---|---|---|---|
| Algoritmo de hash da chave | HMAC-SHA256 com pepper | resolvida | A chave já nasce com alta entropia (256 bits aleatórios) — diferente de senha de usuário, não precisa de um hash memory-hard/lento como Argon2, cujo custo defende principalmente contra senhas fracas e ataques de dicionário. HMAC-SHA256 é suficiente, mais simples e sem dependência externa (`javax.crypto` já no JDK). |
| Onde fica o pepper do HMAC | Variável de ambiente (nome a definir na implementação, ex. `API_KEY_HMAC_PEPPER`), nunca no banco nem no código-fonte | resolvida | Se a tabela de chaves vazar sozinha, os hashes continuam inúteis sem o pepper — motivo de existir separado do salt/hash armazenado. |
| Prefixo da chave gerada | `dak_` ("Deployo API Key") | resolvida | Prefixo genérico do projeto, não amarrado ao primeiro consumidor (serviço de e-mail) — o objetivo declarado é um padrão reutilizável entre APIs internas futuras. |
| Tamanho da chave | 32 bytes de entropia aleatória (256 bits), codificados em base64url, com o prefixo `dak_` concatenado antes | resolvida | Padrão de mercado para tokens de API (GitHub, Stripe usam entropia equivalente ou maior). |
| Mecanismo de acesso a banco (JDBC puro vs Spring Data vs outra biblioteca) | — | **em aberto** | A ferramenta é uma CLI de vida curta — uma dependência leve (JDBC puro, ou um wrapper fino tipo JDBI) pode bastar e evita o custo de start-up de um contexto Spring só para uma inserção. Mas o `jogo-acoes` usa Spring Data JPA; vale avaliar se consistência entre os dois projetos pesa mais que a simplicidade aqui. |
| Motor de banco de dados | Provavelmente PostgreSQL | **em aberto — a confirmar** | Consistente com os perfis `docker`/`staging`/`production` do `jogo-acoes`, mas não confirmado para este projeto especificamente. |
| Ferramenta de migration (Flyway/Liquibase/nenhuma) | — | **em aberto** | Mesma lógica de migrations versionadas já usada no `jogo-acoes`; a confirmar se este projeto replica isso ou usa algo mais simples dado seu tamanho. |

Tarefas que dependem de uma decisão "em aberto" ficam bloqueadas nela em `tasks.md` — a
decisão vira commit `decision:` quando resolvida, atualizando esta tabela no mesmo commit.

## Estrutura de módulos/pacotes

O repositório tem (pelo menos) duas frentes de código, que devem ficar em pacotes/módulos
Maven separados desde já, mesmo com a leitura ainda fora de escopo desta feature:

- **Emissão** (esta feature): gera a chave, calcula o hash, persiste, expõe a CLI.
- **Leitura** (feature futura, biblioteca): só lê pelo hash para validar uma chave recebida
  — usada como dependência pelo serviço de e-mail, sem trazer a lógica de geração/CLI junto.

## Riscos e trade-offs

- **Perda do pepper é irreversível**: sem ele, nenhum hash já persistido pode ser validado
  novamente (não há como re-derivar as chaves originais). Mitigação é operacional (backup do
  valor do pepper), não uma feature de código — registrar isso como procedimento no README ou
  runbook quando a implementação acontecer.
- Deixar o mecanismo de acesso a banco em aberto atrasa a T001 (schema/migration) de
  `tasks.md` até essa decisão ser tomada.
