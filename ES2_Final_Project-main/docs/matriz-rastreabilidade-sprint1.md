# Matriz de Rastreabilidade - Sprint 4

## Resumo

| Dimensao | Cobertura |
| --- | --- |
| Area funcional | Autenticacao, gestao de utilizadores e servicos de dominio GREENHERB |
| Endpoints cobertos | `GET /health`, `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`, `GET /users/me`, `GET /users` |
| Testes unitarios | `85` |
| Testes de integracao | `17` |
| Testes de sistema | `3` |
| Total de testes | `105` |
| Tecnicas aplicadas | Particionamento de Equivalencia, Valores Limite, Fluxo nominal, Condicoes multiplas, MC/DC, Validacao de token JWT, Validacao de payload JSON, Validacao de metodos HTTP, Tratamento de erro |
| Codigos HTTP documentados | `200`, `201`, `400`, `401`, `403`, `404`, `409`, `500` |

## Testes Unitarios

| ID do Caso de Teste | Requisito / Regra de Negocio | Endpoint(s) Exercitado(s) | Nivel de Teste | Tecnica Aplicada | Resultado Esperado | Pre-condicoes |
| --- | --- | --- | --- | --- | --- | --- |
| `TU-001` | `RN-ALERT-01` Medicao dentro do intervalo gera estado nominal | `alertService.classify` | Unidade | Particionamento de Equivalencia | A classificacao devolvida e `OK`. | Limites e valor fornecidos em memoria. |
| `TU-002` | `RN-ALERT-02` Medicao abaixo do minimo gera alerta critico | `alertService.classify` | Unidade | Particionamento de Equivalencia | A classificacao devolvida e `CRITICAL`. | Limites e valor fornecidos em memoria. |
| `TU-003` | `RN-ALERT-03` Medicao acima do maximo gera aviso | `alertService.classify` | Unidade | Particionamento de Equivalencia | A classificacao devolvida e `WARNING`. | Limites e valor fornecidos em memoria. |
| `TU-004` | `RN-ALERT-04` Valores de fronteira sao classificados corretamente | `alertService.classify` | Unidade | Valores Limite | Os valores no limite continuam validos. | Limites e valor fornecidos em memoria. |
| `TU-005` | `RN-ALERT-05` MC/DC para classificacao nominal | `alertService.classify` | Unidade | MC/DC | Valor dentro do intervalo devolve `OK`. | Limites e valor fornecidos em memoria. |
| `TU-006` | `RN-ALERT-06` MC/DC para valor abaixo do limite | `alertService.classify` | Unidade | MC/DC | Valor abaixo do minimo devolve `CRITICAL`. | Limites e valor fornecidos em memoria. |
| `TU-007` | `RN-ALERT-07` MC/DC para valor acima do limite | `alertService.classify` | Unidade | MC/DC | Valor acima do maximo devolve `WARNING`. | Limites e valor fornecidos em memoria. |
| `TU-008` | `RN-AUDIT-01` Auditoria regista acao valida | `auditService.log` | Unidade | Fluxo nominal | O log devolvido contem `user`, `action` e `timestamp`. | Dados validos em memoria. |
| `TU-009` | `RN-AUDIT-02` Auditoria rejeita campos em falta | `auditService.log` | Unidade | Particionamento de Equivalencia | E lancado erro para pedido incompleto. | Nenhuma. |
| `TU-010` | `RN-MW-01` Middleware autentica bearer token valido | `authMiddleware.authenticate` | Unidade | Fluxo nominal | `req.user` e preenchido e `next()` e chamado sem erro. | Access token valido emitido. |
| `TU-011` | `RN-MW-02` Middleware rejeita ausencia de bearer token | `authMiddleware.authenticate` | Unidade | Particionamento de Equivalencia | O middleware encaminha erro `401`. | Nenhuma. |
| `TU-012` | `RN-MW-03` Middleware rejeita bearer token invalido | `authMiddleware.authenticate` | Unidade | Particionamento de Equivalencia | O middleware encaminha erro `401`. | Nenhuma. |
| `TU-013` | `RN-MW-04` Middleware autoriza perfil valido | `authMiddleware.authorize` | Unidade | Fluxo nominal | `next()` e chamado sem erro para perfil permitido. | `req.user.role` autorizado. |
| `TU-014` | `RN-MW-05` Middleware rejeita perfil sem permissao | `authMiddleware.authorize` | Unidade | Particionamento de Equivalencia | O middleware encaminha erro `403`. | `req.user.role` nao autorizado. |
| `TU-015` | `RN-AUTH-01` Registo de utilizador com dados validos | `authService.register` | Unidade | Particionamento de Equivalencia | O utilizador e criado e sao devolvidos `accessToken` e `refreshToken`. | Repositorio em memoria limpo. |
| `TU-016` | `RN-AUTH-02` Email repetido e rejeitado | `authService.register` | Unidade | Particionamento de Equivalencia | A operacao falha com `User already exists.` | Ja existe utilizador com o mesmo email. |
| `TU-017` | `RN-AUTH-03` Perfil fora do enum e rejeitado | `authService.register` | Unidade | Particionamento de Equivalencia | A operacao falha com `Role must be TECHNICIAN, MANAGER or ADMIN.` | Nenhuma. |
| `TU-018` | `RN-AUTH-04` Nome demasiado curto e rejeitado | `authService.register` | Unidade | Valores Limite | A operacao falha com `Name must contain at least 3 characters.` | Nenhuma. |
| `TU-019` | `RN-AUTH-05` Email invalido no registo e rejeitado | `authService.register` | Unidade | Particionamento de Equivalencia | A operacao falha com `Email is invalid.` | Nenhuma. |
| `TU-020` | `RN-AUTH-06` Password curta no registo e rejeitada | `authService.register` | Unidade | Valores Limite | A operacao falha com `Password must contain at least 8 characters.` | Nenhuma. |
| `TU-021` | `RN-AUTH-07` Payload ausente no registo e rejeitado | `authService.register` | Unidade | Particionamento de Equivalencia | A operacao falha com erro `400`. | Nenhuma. |
| `TU-022` | `RN-AUTH-08` Login com credenciais validas | `authService.login` | Unidade | Particionamento de Equivalencia | O login devolve utilizador autenticado e novo par de tokens. | Utilizador previamente registado. |
| `TU-023` | `RN-AUTH-09` Login com password vazia e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Password is required.` | Utilizador previamente registado. |
| `TU-024` | `RN-AUTH-10` Login com password incorreta e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Invalid credentials.` | Utilizador previamente registado. |
| `TU-025` | `RN-AUTH-11` Login com email inexistente e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Invalid credentials.` | Utilizador nao existe. |
| `TU-026` | `RN-AUTH-12` Login com email invalido e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Email is invalid.` | Nenhuma. |
| `TU-027` | `RN-AUTH-13` Login com ambos os campos vazios e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Email and password are required.` | Nenhuma. |
| `TU-028` | `RN-AUTH-14` Login sem email e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com `Email is required.` | Nenhuma. |
| `TU-029` | `RN-AUTH-15` Login sem payload e rejeitado | `authService.login` | Unidade | Particionamento de Equivalencia | A autenticacao falha com erro `400`. | Nenhuma. |
| `TU-030` | `RN-AUTH-16` Refresh token valido gera nova sessao | `authService.refresh` | Unidade | Fluxo nominal | Sao devolvidos novos tokens e o refresh anterior deixa de ser reutilizavel. | Refresh token emitido. |
| `TU-031` | `RN-AUTH-17` Refresh sem token e rejeitado | `authService.refresh` | Unidade | Particionamento de Equivalencia | A API responde com `Refresh token is required.` | Nenhuma. |
| `TU-032` | `RN-AUTH-18` Refresh sem payload e rejeitado | `authService.refresh` | Unidade | Particionamento de Equivalencia | A API responde com erro `400`. | Nenhuma. |
| `TU-033` | `RN-AUTH-19` Refresh com token invalido e rejeitado | `authService.refresh` | Unidade | Particionamento de Equivalencia | A API responde com `Refresh token is invalid or expired.` | Nenhuma. |
| `TU-034` | `RN-AUTO-01` Regra automatica executa em modo automatico com condicao verdadeira | `automationService.run` | Unidade | Fluxo nominal | O servico devolve `executed: true`. | `mode=AUTOMATIC`, `condition=true`. |
| `TU-035` | `RN-AUTO-02` Regra nao executa com condicao falsa | `automationService.run` | Unidade | Particionamento de Equivalencia | O servico devolve `executed: false`. | `condition=false`. |
| `TU-036` | `RN-AUTO-03` Execucao em modo manual e rejeitada | `automationService.run` | Unidade | Particionamento de Equivalencia | E lancado erro `Cannot run automation in manual mode.` | `mode=MANUAL`, `condition=true`. |
| `TU-037` | `RN-AUTO-04` MC/DC manual com condicao falsa nao executa | `automationService.run` | Unidade | MC/DC | O servico devolve `executed: false`. | `mode=MANUAL`, `condition=false`. |
| `TU-038` | `RN-AUTO-05` MC/DC automatico com condicao verdadeira executa | `automationService.run` | Unidade | MC/DC | O servico devolve `executed: true`. | `mode=AUTOMATIC`, `condition=true`. |
| `TU-039` | `RN-AUTO-06` MC/DC automatico com condicao falsa nao executa | `automationService.run` | Unidade | MC/DC | O servico devolve `executed: false`. | `mode=AUTOMATIC`, `condition=false`. |
| `TU-040` | `RN-BATCH-01` Criacao valida de lote | `batchService.create` | Unidade | Fluxo nominal | O lote e devolvido com `id`, `planId`, `status` e `productivity`. | Dados validos em memoria. |
| `TU-041` | `RN-BATCH-02` Lote sem id e rejeitado | `batchService.create` | Unidade | Particionamento de Equivalencia | E lancado erro `Batch id is required.` | Nenhuma. |
| `TU-042` | `RN-BATCH-03` Lote com estado invalido e rejeitado | `batchService.create` | Unidade | Particionamento de Equivalencia | E lancado erro `Invalid batch status.` | Nenhuma. |
| `TU-043` | `RN-BATCH-04` Lote com produtividade negativa e rejeitado | `batchService.create` | Unidade | Valores Limite | E lancado erro `Productivity must be positive.` | Nenhuma. |
| `TU-044` | `RN-HERB-01` Importacao valida de CSV de ervas | `herbService.import` | Unidade | Fluxo nominal | O resultado contabiliza linhas validas. | CSV valido em memoria. |
| `TU-045` | `RN-HERB-02` Linhas invalidas sao contabilizadas | `herbService.import` | Unidade | Particionamento de Equivalencia | O resultado incrementa `invalid`. | CSV com linhas incompletas. |
| `TU-046` | `RN-HERB-03` Ficheiro misto contabiliza validas e invalidas | `herbService.import` | Unidade | Particionamento de Equivalencia | O resultado distingue `valid`, `invalid` e `total`. | CSV misto em memoria. |
| `TU-047` | `RN-HERB-04` Ficheiro vazio e rejeitado | `herbService.import` | Unidade | Particionamento de Equivalencia | E lancado erro `File is empty.` | Conteudo vazio. |
| `TU-048` | `RN-HERB-05` Ficheiro so com cabecalho e rejeitado | `herbService.import` | Unidade | Particionamento de Equivalencia | E lancado erro `File is empty.` | CSV apenas com cabecalho. |
| `TU-049` | `RN-HERB-06` Espacos extra nao invalidam parsing correto | `herbService.import` | Unidade | Particionamento de Equivalencia | As linhas continuam a ser processadas corretamente. | CSV com whitespace extra. |
| `TU-050` | `RN-MEASURE-01` Medicao valida e aceite | `measurementService.validate` | Unidade | Fluxo nominal | O resultado devolve `valid: true`. | Temperatura, humidade e luminosidade fornecidas. |
| `TU-051` | `RN-MEASURE-02` Medicao com dados em falta e rejeitada | `measurementService.validate` | Unidade | Particionamento de Equivalencia | E lancado erro `Missing measurement data.` | Falta pelo menos um parametro. |
| `TU-052` | `RN-PLAN-01` Plano regular valido e aceite | `planService.validate` | Unidade | Fluxo nominal | O plano devolvido fica com `validated: true`. | Dados validos em memoria. |
| `TU-053` | `RN-PLAN-02` Tipo de plano invalido e rejeitado | `planService.validate` | Unidade | Particionamento de Equivalencia | E lancado erro `Invalid plan type`. | Nenhuma. |
| `TU-054` | `RN-PLAN-03` Limites de temperatura sao validados | `planService.validate` | Unidade | Valores Limite | Valores 17 e 29 falham; 18 e 28 passam. | Plano base valido. |
| `TU-055` | `RN-PLAN-04` Limites de humidade sao validados | `planService.validate` | Unidade | Valores Limite | Valores 39 e 81 falham; 40 e 80 passam. | Plano base valido. |
| `TU-056` | `RN-PLAN-05` Limites de luminosidade sao validados | `planService.validate` | Unidade | Valores Limite | Valores 4999 e 25001 falham; 5000 e 25000 passam. | Plano base valido. |
| `TU-057` | `RN-PLAN-06` Limites de duracao sao validados | `planService.validate` | Unidade | Valores Limite | Valores 0 e 366 falham; 1 e 365 passam. | Plano base valido. |
| `TU-058` | `RN-PLAN-07` Plano pontual sem autorizacao e rejeitado | `planService.validate` | Unidade | Condicoes Multiplas | E lancado erro `Authorization required`. | `type=punctual`, `approvedByManager=false`. |
| `TU-059` | `RN-PLAN-08` Plano pontual com autorizacao e aceite | `planService.validate` | Unidade | Condicoes Multiplas | O plano e validado com sucesso. | `type=punctual`, `approvedByManager=true`. |
| `TU-060` | `RN-PLAN-09` Condicoes combinadas de criacao de plano sao validadas | `planService.validate` | Unidade | Condicoes Multiplas | Sem autorizacao falha; com autorizacao passa. | Plano pontual com parametros validos. |
| `TU-061` | `RN-PLAN-10` Estrutura vazia de plano e rejeitada | `planService.validate` | Unidade | Particionamento de Equivalencia | E lancado erro `Invalid plan data.` | Nenhuma. |
| `TU-062` | `RN-PLAN-11` MC/DC nao pontual sem autorizacao passa | `planService.validate` | Unidade | MC/DC | Plano regular sem autorizacao explicita continua valido. | `type=regular`, `approvedByManager=false`. |
| `TU-063` | `RN-PLAN-12` MC/DC pontual sem autorizacao falha | `planService.validate` | Unidade | MC/DC | E lancado erro `Authorization required`. | `type=punctual`, `approvedByManager=false`. |
| `TU-064` | `RN-PLAN-13` MC/DC pontual com autorizacao passa | `planService.validate` | Unidade | MC/DC | O plano e validado com sucesso. | `type=punctual`, `approvedByManager=true`. |
| `TU-065` | `RN-REPORT-01` Relatorio CSV e gerado com dados validos | `reportService.generate` | Unidade | Fluxo nominal | O resultado devolve `format` e `exported: true`. | `format=CSV` e dados presentes. |
| `TU-066` | `RN-REPORT-02` Relatorio sem dados e rejeitado | `reportService.generate` | Unidade | Particionamento de Equivalencia | E lancado erro `No data to export.` | `data=[]` ou ausente. |
| `TU-067` | `RN-REPORT-03` Formato invalido e rejeitado | `reportService.generate` | Unidade | Particionamento de Equivalencia | E lancado erro `Invalid format.` | Dados presentes. |
| `TU-068` | `RN-TASK-01` Tarefa valida e executada | `taskService.execute` | Unidade | Fluxo nominal | O resultado devolve `executed: true` e a data de execucao. | `batchId`, `type` e `date` validos. |
| `TU-069` | `RN-TASK-02` Tipo de tarefa invalido e rejeitado | `taskService.execute` | Unidade | Particionamento de Equivalencia | E lancado erro `Invalid task type.` | Nenhuma. |
| `TU-070` | `RN-TASK-03` Tarefa ja executada e rejeitada | `taskService.execute` | Unidade | Particionamento de Equivalencia | E lancado erro `Task already executed.` | `executed=true`. |
| `TU-071` | `RN-TASK-04` Tarefa sem data e rejeitada | `taskService.execute` | Unidade | Particionamento de Equivalencia | E lancado erro `Execution date is required.` | `date` ausente. |
| `TU-072` | `RN-TOKEN-01` Emissao de tokens usa payload correto | `tokenService.issueTokens` | Unidade | Fluxo nominal | `accessToken` e `refreshToken` contêm `sub`, `email`, `role` e `jti`. | Utilizador em memoria. |
| `TU-073` | `RN-TOKEN-02` Refresh token valido roda para novo par | `tokenService.rotateRefreshToken` | Unidade | Fluxo nominal | E devolvido um novo par de tokens. | Refresh token emitido. |
| `TU-074` | `RN-TOKEN-03` Reutilizacao de refresh token e rejeitada | `tokenService.rotateRefreshToken` | Unidade | Particionamento de Equivalencia | A reutilizacao devolve `null`. | Refresh token previamente rodado. |
| `TU-075` | `RN-TOKEN-04` Access token valido pode ser verificado | `tokenService.verifyAccessToken` | Unidade | Fluxo nominal | O payload devolvido corresponde ao utilizador autenticado. | Access token emitido. |
| `TU-076` | `RN-TOKEN-05` Limpeza de refresh tokens invalida sessoes antigas | `tokenService.clearRefreshTokens` | Unidade | Fluxo nominal | Um refresh token antigo deixa de ser valido. | Refresh token previamente emitido. |
| `TU-077` | `RN-REPO-01` Criacao de utilizador normaliza email | `userRepository.create` | Unidade | Particionamento de Equivalencia | O email fica em lowercase e o utilizador recebe `id` e `createdAt`. | Repositorio limpo. |
| `TU-078` | `RN-REPO-02` Pesquisa por email ignora maiusculas/minusculas | `userRepository.findByEmail` | Unidade | Particionamento de Equivalencia | O utilizador e encontrado com casing diferente. | Utilizador previamente criado. |
| `TU-079` | `RN-REPO-03` Pesquisa por id encontra utilizador existente | `userRepository.findById` | Unidade | Fluxo nominal | O utilizador correspondente e devolvido. | Utilizador previamente criado. |
| `TU-080` | `RN-REPO-04` Pesquisa por id inexistente devolve `null` | `userRepository.findById` | Unidade | Particionamento de Equivalencia | O resultado e `null`. | Repositorio limpo ou id inexistente. |
| `TU-081` | `RN-REPO-05` Listagem devolve utilizadores saneados | `userRepository.list` | Unidade | Fluxo nominal | Os utilizadores sao devolvidos sem `passwordHash`. | Pelo menos dois utilizadores criados. |
| `TU-082` | `RN-REPO-06` Limpeza do repositorio remove todos os utilizadores | `userRepository.clear` | Unidade | Fluxo nominal | A listagem fica vazia apos limpeza. | Pelo menos um utilizador criado. |
| `TU-083` | `RN-CTRL-01` Controlador devolve o utilizador autenticado | `usersController.me` | Unidade | Fluxo nominal | O controlador responde com `200` e utilizador saneado. | `req.user.sub` valido. |
| `TU-084` | `RN-CTRL-02` Controlador propaga falha ao consultar o utilizador | `usersController.me` | Unidade | Tratamento de erro | O controlador chama `next(error)`. | Falha simulada no repositorio. |
| `TU-085` | `RN-CTRL-03` Controlador lista utilizadores e propaga erros | `usersController.list` | Unidade | Fluxo nominal + Tratamento de erro | A listagem devolve `200`; em falha chama `next(error)`. | Repositorio simulado com sucesso e falha em testes separados. |

## Testes de Integracao

| ID do Caso de Teste | Requisito / Regra de Negocio | Endpoint(s) Exercitado(s) | Nivel de Teste | Tecnica Aplicada | Resultado Esperado | Pre-condicoes |
| --- | --- | --- | --- | --- | --- | --- |
| `TI-001` | `RN-API-01` Endpoint de saude devolve disponibilidade da API | `GET /health` | Integracao | Fluxo nominal | A API responde com `200`, `status: ok` e `service: greenherb-api`. | Aplicacao iniciada. |
| `TI-002` | `RN-AUTH-20` Registo cria utilizador e permite acesso autenticado | `POST /auth/register` + `GET /users/me` | Integracao | Fluxo nominal | O registo devolve `201` e o endpoint protegido devolve `200`. | Repositorio limpo. |
| `TI-003` | `RN-AUTH-21` Endpoint protegido aceita JWT valido | `POST /auth/register` + `GET /users/me` | Integracao | Particionamento de Equivalencia | A API aceita bearer token valido e devolve o utilizador autenticado. | Utilizador criado no proprio teste. |
| `TI-004` | `RN-AUTH-22` Endpoint protegido rejeita JWT invalido | `GET /users/me` | Integracao | Validacao de token JWT | A API responde com `401` e mensagem de token invalido. | Aplicacao iniciada. |
| `TI-005` | `RN-AUTH-23` Endpoint protegido exige bearer token | `GET /users/me` | Integracao | Particionamento de Equivalencia | A API responde com `401` e mensagem `Bearer token is required.` | Aplicacao iniciada. |
| `TI-006` | `RN-API-02` Falha interna inesperada e tratada pela API | `GET /users/me` | Integracao | Tratamento de erro | A API responde com `500` e mensagem `Internal server error.` | Utilizador autenticado e falha simulada no repositorio. |
| `TI-007` | `RN-AUTH-24` JSON malformado no registo e rejeitado | `POST /auth/register` | Integracao | Validacao de payload JSON | A API responde com `400` e mensagem `Malformed JSON payload.` | Header `Content-Type: application/json`. |
| `TI-008` | `RN-AUTH-25` Login sem credenciais e rejeitado | `POST /auth/login` | Integracao | Particionamento de Equivalencia | A API responde com `400` e mensagem `Email and password are required.` | Nenhuma. |
| `TI-009` | `RN-AUTH-26` Refresh token valido roda sessao e invalida token anterior | `POST /auth/refresh` | Integracao | Fluxo nominal | O primeiro refresh devolve `200`; a reutilizacao do token anterior devolve `401`. | Utilizador autenticado com refresh token emitido. |
| `TI-010` | `RN-AUTH-27` Refresh token malformado e rejeitado | `POST /auth/refresh` | Integracao | Particionamento de Equivalencia | A API responde com `401` e mensagem `Refresh token is invalid or expired.` | Nenhuma. |
| `TI-011` | `RN-AUTH-28` Metodo HTTP nao suportado e rejeitado | `GET /auth/login` | Integracao | Validacao de metodos HTTP | A API responde com `404`. | Aplicacao iniciada. |
| `TI-012` | `RN-USER-01` `/users/me` aceita bearer token valido | `POST /auth/register` + `GET /users/me` | Integracao | Fluxo nominal | O endpoint devolve `200` e os dados do utilizador autenticado. | Utilizador criado no proprio teste. |
| `TI-013` | `RN-USER-02` `/users/me` rejeita ausencia de token | `GET /users/me` | Integracao | Particionamento de Equivalencia | O endpoint devolve `401` sem bearer token. | Aplicacao iniciada. |
| `TI-014` | `RN-USER-03` `/users/me` rejeita esquema de autenticacao diferente de Bearer | `GET /users/me` | Integracao | Particionamento de Equivalencia | O endpoint devolve `401` com header `Basic`. | Aplicacao iniciada. |
| `TI-015` | `RN-USER-04` `/users/me` rejeita JWT invalido | `GET /users/me` | Integracao | Validacao de token JWT | O endpoint devolve `401` com token invalido. | Aplicacao iniciada. |
| `TI-016` | `RN-USER-05` `/users` e permitido a administrador | `POST /auth/register` + `GET /users` | Integracao | Controlo de acesso por perfil | A API devolve `200` e lista de utilizadores para `ADMIN`. | Um `ADMIN` autenticado e pelo menos mais um utilizador criado. |
| `TI-017` | `RN-USER-06` `/users` e proibido para perfil nao administrador | `POST /auth/register` + `GET /users` | Integracao | Controlo de acesso por perfil | A API devolve `403` e mensagem `Insufficient permissions.` | Um `MANAGER` autenticado. |

## Testes de Sistema

| ID do Caso de Teste | Requisito / Regra de Negocio | Endpoint(s) Exercitado(s) | Nivel de Teste | Tecnica Aplicada | Resultado Esperado | Pre-condicoes |
| --- | --- | --- | --- | --- | --- | --- |
| `TS-001` | `RN-SYS-01` Fluxo completo de autenticacao | `POST /auth/register` + `POST /auth/login` + `GET /users/me` | Sistema | Fluxo end-to-end | O utilizador e registado, autenticado e consulta o proprio perfil com sucesso. | Aplicacao iniciada e repositorio limpo. |
| `TS-002` | `RN-SYS-02` Fluxo de renovacao de sessao | `POST /auth/register` + `POST /auth/refresh` + `GET /users/me` | Sistema | Fluxo end-to-end | O utilizador obtem novos tokens e continua a aceder ao endpoint protegido. | Aplicacao iniciada e repositorio limpo. |
| `TS-003` | `RN-SYS-03` Controlo de acesso em fluxo realista | `GET /users/me` | Sistema | Fluxo end-to-end | O sistema bloqueia o acesso sem token ou com token invalido. | Aplicacao iniciada. |

## Observacoes

- A matriz segue a estrutura pedida no enunciado: `ID`, `Requisito / Regra de Negocio`, `Endpoint(s) Exercitado(s)`, `Nivel de Teste`, `Tecnica Aplicada`, `Resultado Esperado` e `Pre-condicoes`.
- O Sprint 4 fica agora alinhado com o estado real do repositório: a API exposta continua concentrada em `/auth`, `/users` e `/health`, enquanto os restantes modulos de dominio estao cobertos ao nivel unitario.
- Os testes atuais passam integralmente com `npm test`, totalizando `105` casos (`85` unidade, `17` integracao, `3` sistema).
