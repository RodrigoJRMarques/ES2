# GREENHERB - Sprint 4

Estado atual do projeto GREENHERB com foco no Sprint 4 do enunciado:

- testes de integracao dos endpoints existentes com `supertest`;
- cobertura de payloads JSON validos e invalidos;
- validacao de headers `Authorization`, metodos HTTP e respostas de erro;
- testes unitarios dos servicos de dominio atualmente presentes no repositório;
- atualizacao da matriz de rastreabilidade.

## Endpoints implementados

- `GET /health` - estado da API.
- `POST /auth/register` - regista um utilizador.
- `POST /auth/login` - autentica um utilizador.
- `POST /auth/refresh` - renova tokens JWT.
- `GET /users/me` - devolve o utilizador autenticado.
- `GET /users` - lista utilizadores, apenas para perfil `ADMIN`.

## Modulos de dominio atualmente presentes

- `alertService`
- `auditService`
- `automationService`
- `batchService`
- `herbService`
- `measurementService`
- `planService`
- `reportService`
- `taskService`

## Cobertura de testes

- `tests/unit` contem `85` testes unitarios.
- `tests/integration` contem `17` testes de integracao.
- `tests/system` contem `3` testes de sistema.
- Total atual: `105` testes.

## Como executar

1. Copiar `.env.example` para `.env`.
2. Instalar dependencias com `npm install`.
3. Iniciar a API com `npm run dev`.

## Como testar

- Executar `npm test`.

## Notas de implementacao

- O repositorio de utilizadores continua em memoria.
- Os refresh tokens sao guardados em memoria e invalidados na rotacao.
- O `errorHandler` trata JSON malformado com resposta `400`.
- O Sprint 4 cobre os endpoints atualmente expostos; os restantes modulos presentes no repositório ainda estao cobertos ao nivel unitario.
