# Análise do Projeto `Trabalho Prático/Sprint1`

## Visão geral

O projeto implementa um sistema de logging modular em Java, onde vários padrões de desenho são combinados para suportar:

- criação de logs por tipo;
- configuração global centralizada;
- envio para múltiplos destinos;
- categorização hierárquica de logs;
- reutilização de objetos formatadores;
- persistência e restauro do estado da configuração;
- extensão do comportamento após o dispatch sem alterar o dispatcher.

## Estrutura principal

### `config`

- `LogConfig`: classe central do sistema, implementada como `Singleton`.
- `LogLevel`: enum com os níveis `DEBUG`, `INFO`, `WARNING`, `ERROR`.
- `LogDestination`: enum com os destinos `CONSOLE`, `FILE`, `DATABASE`, `REMOTE`.
- `LogConfigSnapshot`: interface usada como marcador para o padrão `Memento`.

Responsabilidades:

- guardar o nível e destino principal;
- manter níveis ativos;
- manter destinos ativos;
- gerir filtros;
- criar e restaurar snapshots internos.

### `logs`

- `LogEntry`: classe abstrata base para qualquer log.
- `InfoLog`, `WarningLog`, `ErrorLog`, `DebugLog`: especializações concretas.

Responsabilidades:

- guardar mensagem e timestamp;
- devolver o nível do log;
- formatar o conteúdo do log.

### `factory`

- `LogFactory`: cria instâncias de `LogEntry` com base no tipo recebido.
- `LogDestinationFactory`: cria destinos concretos com base em `LogDestination`.

Responsabilidades:

- encapsular a lógica de criação de objetos;
- evitar instanciação direta espalhada pelo sistema.

### `destinations`

- `LogDestinationImplementor`: interface do lado implementador do `Bridge`.
- `ConsoleLogDestination`, `FileLogDestination`, `DatabaseLogDestination`, `RemoteLogDestination`: implementações concretas.

Responsabilidades:

- abstrair o mecanismo físico/lógico de escrita;
- permitir trocar o destino sem alterar o fluxo principal do envio.

### `service`

- `LogBridge`: abstração que conhece um `LogDestinationImplementor`.
- `ApplicationLogBridge`: especialização concreta da abstração.
- `LogDispatcher`: coordenador do envio dos logs.

Responsabilidades do `LogDispatcher`:

- validar o nível do log;
- aplicar filtros;
- obter destinos ativos da configuração;
- enviar logs através do `Bridge`;
- acionar a cadeia de decorators pós-dispatch.

### `decorators`

- `DispatchAction`: interface para ações associadas ao processamento do dispatch.
- `NoOpDispatchAction`: implementação neutra.
- `DispatchActionDecorator`: decorator base.
- `AdminAlertDecorator`, `MonitoringIntegrationDecorator`, `ErrorPatternAnalysisDecorator`: decorators concretos.

Responsabilidades:

- acrescentar comportamento sem alterar `LogDispatcher`;
- reagir a logs enviados ou filtrados;
- suportar composição em cadeia.

### `filters`

- `LogFilter`: interface de filtro.
- `KeywordExcludeFilter`: filtro concreto por palavras bloqueadas.

Responsabilidades:

- decidir se um `LogEntry` deve ou não passar;
- tornar as regras de filtragem extensíveis.

### `composite`

- `LogComponent`: componente base do `Composite`.
- `LogCategory`: nó composto.
- `LogLeaf`: folha que encapsula um `LogEntry`.

Responsabilidades:

- organizar logs numa estrutura hierárquica;
- permitir tratamento uniforme entre categorias e folhas.

### `object_pool`

- `FormatterPool`: pool singleton de `LogFormatter`.
- `LogFormatter`: formatador reutilizável.

Responsabilidades:

- reduzir criação repetida de formatadores;
- disponibilizar aquisição e libertação controladas.

### `state`

- `LogStateManager`: caretaker do padrão `Memento`.

Responsabilidades:

- guardar snapshots da configuração;
- restaurar estados anteriores de `LogConfig`.

## Padrões de desenho identificados

1. `Singleton`
   - `LogConfig`
   - `FormatterPool`

2. `Factory`
   - `LogFactory`
   - `LogDestinationFactory`

3. `Bridge`
   - `LogBridge` / `ApplicationLogBridge`
   - `LogDestinationImplementor` e implementações concretas

4. `Composite`
   - `LogComponent`
   - `LogCategory`
   - `LogLeaf`

5. `Object Pool`
   - `FormatterPool`
   - `LogFormatter`

6. `Memento`
   - `LogConfig`
   - `LogConfigSnapshot`
   - `LogStateManager`

7. `Decorator`
   - `DispatchAction`
   - `DispatchActionDecorator`
   - decorators concretos

## Fluxo resumido

1. `Main` obtém `LogConfig` e configura o sistema.
2. `LogFactory` cria instâncias de `LogEntry`.
3. `LogDispatcher` valida o log com base em `LogConfig`.
4. `LogDestinationFactory` cria o destino apropriado.
5. `LogBridge` envia o conteúdo formatado para o destino.
6. A cadeia de `DispatchActionDecorator` executa comportamento adicional.
7. `LogStateManager` pode guardar e restaurar o estado da configuração.

## Observações técnicas

- O projeto está organizado por módulos e padrões, o que facilita a leitura.
- `LogDispatcher` concentra a orquestração do envio e é o ponto de integração entre vários módulos.
- `LogConfig` está relativamente carregada de responsabilidades, mas isso é coerente com o papel de configuração global do sistema.
- O diagrama de classes deve destacar sobretudo as relações entre:
  - `LogDispatcher`, `LogBridge` e `LogDestinationImplementor`;
  - `LogConfig`, `LogStateManager` e `LogConfigSnapshot`;
  - `DispatchActionDecorator` e os decorators concretos;
  - `LogEntry` e as subclasses concretas.
