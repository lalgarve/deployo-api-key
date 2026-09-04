# deployo-api-key

Biblioteca e aplicação Java para gerar API-KEY pela linha de comando.

## Contexto e propósito

Este projeto nasce dentro do ecossistema do [`jogo-acoes`](https://github.com/lalgarve/jogo-acoes),
que hoje autentica usuários por link mágico enviado por e-mail. O envio de e-mail está sendo
extraído para um serviço próprio — e esse serviço precisa de uma forma de restringir quem
pode chamá-lo. A solução escolhida é autenticação via API-KEY.

Em vez de resolver isso só para o serviço de e-mail, o objetivo é ter uma forma **padrão** de
gerar e validar API-KEY em qualquer API interna — o serviço de e-mail é só o primeiro
consumidor. Como o número de serviços que vão precisar disso é pequeno por enquanto (1), uma
interface de administração foi conscientemente deixada de fora do escopo inicial: a
linha de comando já resolve o problema real sem o custo de construir e manter uma UI que
ninguém usaria ainda.

Por ser um projeto pequeno e autocontido, ele também serve como **exemplo compacto de
Spec-Driven Development (SDD)** — a metodologia usada aqui, mais fácil de avaliar de ponta a
ponta do que o `jogo-acoes` (que já é grande e usa uma abordagem correlata, BDD + DER + OpenAPI
escritos antes da implementação, mas não formalizada como SDD). A intenção é usar este
repositório para pedir ao professor da disciplina autorização para adotar SDD como
metodologia — ver o [documento de alinhamento do jogo-acoes](https://github.com/lalgarve/jogo-acoes/blob/docs/alinhamento-projeto-disciplina/docs/context/alinhamento-projeto-disciplina.md)
para o contexto acadêmico completo.

## Metodologia de desenvolvimento

Este projeto usa Spec-Driven Development (SDD):

- [`memory/constitution.md`](memory/constitution.md) — convenções do projeto (idioma,
  commits, branches/PR, testes, rastreamento de trabalho via Issues).
- [`specs/`](specs/) — spec, plano técnico e tarefas de cada feature, escritos antes da
  implementação.
- [`templates/`](templates/) — modelos usados para começar uma feature nova.

## Uso de ferramentas de IA

Conforme a política "Sinal Verde" da disciplina (mesma adotada no `jogo-acoes`), o
desenvolvimento deste projeto conta com apoio de ferramentas de IA (Claude Code, Anthropic)
— incluindo a própria definição da estrutura de SDD usada aqui. Todo conteúdo gerado ou
revisado com apoio de IA é lido, entendido e validado antes de ser incorporado ao
repositório; commits e decisões de arquitetura permanecem de responsabilidade da autora.
