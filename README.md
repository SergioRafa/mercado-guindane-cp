# Mercado Guindane CP - API (`minimercado-v2`)

API REST desenvolvida em **Java 21** e **Spring Boot 4.1.1** para gestao de produtos, estoque, vendas, cancelamento com estorno e resumo financeiro de minimercado, com persistencia em **PostgreSQL (Supabase)** e deploy conteinerizado no **Render**.

- **URL em Producao:** `https://mercado-guindane-cp-api.onrender.com`
- **Repositorio:** `https://github.com/SergioRafa/mercado-guindane-cp`

## Endpoints Principais

- `GET /` e `GET /api/status` — Status da API
- `GET /produtos`, `POST /produtos`, `PUT /produtos/{id}`, `PATCH /produtos/{id}/baixa`, `DELETE /produtos/{id}`
- `GET /produtos/financeiro/resumo` — Resumo financeiro do estoque
- `POST /vendas`, `GET /vendas`, `GET /vendas/{id}`, `PATCH /vendas/{id}/cancelar`

## Variaveis de Ambiente e CORS

- `APP_CORS_ALLOWED_ORIGINS` — Origens permitidas no CORS (ex.: `http://localhost:5173,http://localhost:3000,http://127.0.0.1:5500`)
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`