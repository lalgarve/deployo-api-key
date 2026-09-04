# Specs

Cada feature não trivial ganha uma pasta `NNN-nome-da-feature/` aqui (numeração sequencial
de 3 dígitos, nome em kebab-case), copiada a partir de `templates/`:

```
specs/
  001-nome-da-feature/
    spec.md            # requisitos + critérios de aceite (o QUÊ e POR QUÊ)
    plan.md             # decisões técnicas (o COMO)
    data-model.md         # só se a feature tiver dados persistentes
    contracts/
      cli-commands.md       # contrato de CLI, quando a feature expõe comandos
    tasks.md                # tarefas, espelhadas como Issues no GitHub
```

Ver `memory/constitution.md` (seções "Documentação viva por feature" e "Rastreamento de
trabalho via Issues") para o fluxo completo: `spec.md`/`plan.md` são escritos antes do
código, cada feature tem uma Issue-épico no GitHub, e `tasks.md` é a fonte de verdade das
tarefas que essa Issue rastreia.
