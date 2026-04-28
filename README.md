# FX Event Platform

Event-driven FX rate platform: poll the [AwesomeAPI](https://economia.awesomeapi.com.br) (Brazil), publish changes to **Apache Kafka**, persist snapshots in **MongoDB**, and expose **HTTP APIs** for latest rates and cross-currency conversion.

## Modules

| Module | Role |
|--------|------|
| **exchange-rate-service** | Scheduled fetch of available pairs and last quotes (batched HTTP), in-memory cache, Kafka producer (`exchange-rate-events`). |
| **fx-processing-service** | Kafka consumer; normalizes events and upserts documents in MongoDB collection `exchange_rates`. |
| **api-gateway** | Reads from MongoDB; exposes `/rates`, `/rates/all` (paginated), `/convert`; uses in-memory graph for cross-rate paths. |

Stack: **Java 21**, **Spring Boot 3.x**, **MongoDB**, **Kafka (KRaft)**.

## Prerequisites

- **Docker** and **Docker Compose** (recommended for full stack).
- Or **JDK 21** and **Maven 3.9+** for local JVM runs plus your own Kafka and MongoDB.

## Repo notes

- O `.gitignore` ignora apenas a pasta `out/` na **raiz** (`/out/`) para não esconder pacotes Java como `.../application/port/out/...` (isso quebrava builds quando arquivos “port out” não iam para o git).

## Run with Docker Compose

From the repository root:

```bash
docker compose up --build
```

Services and default host ports:

| Service | Port | Notes |
|---------|------|--------|
| **api-gateway** | 8080 | REST API |
| **exchange-rate-service** | 8081 | State endpoint, producer to Kafka |
| **fx-processing-service** | 8082 | Actuator; consumes Kafka |
| **mongodb** | 27017 | Database `fx` |
| **kafka** | 9092 | PLAINTEXT |

- **Health**: `http://localhost:8080/actuator/health` (and 8081, 8082).

### Example requests

```bash
curl "http://localhost:8080/rates?pair=USD/BRL"

curl -s "http://localhost:8080/rates/all?page=0&size=20&sort=pair,asc"

curl -s -X POST http://localhost:8080/convert \
  -H "Content-Type: application/json" \
  -d '{"from":"USD","to":"BRL","amount":100}'

curl -s http://localhost:8081/exchange-rate/state
```

## Configuration highlights

- **exchange-rate-service**
  - **Kafka**: `KAFKA_BOOTSTRAP_SERVERS` (Spring `spring.kafka.bootstrap-servers`).
  - **Tópico**: `FX_KAFKA_TOPIC` (opcional; default `exchange-rate-events`).
  - **AwesomeAPI**
    - URLs: `FX_AWESOME_API_AVAILABLE_JSON_URL`, `FX_AWESOME_API_LAST_QUOTES_BASE_URL` (opcional; defaults no `application.yml`).
    - Token (query `token`): `FX_AWESOME_API_TOKEN` → vira `...?token=...` automaticamente nos GETs.
  - **Polling**
    - Intervalo: `FX_POLL_API_INTERVAL_MS` → `fx.poll.api-interval-ms`.
    - Catálogo (modo “available”): `FX_POLL_MAX_PAIRS`, `FX_POLL_AVAILABLE_REFRESH_MINUTES`.
    - **Pares fixos USD** (recomendado pra reduzir chamadas): `FX_POLL_FIXED_USD_QUOTES` (CSV tipo `BRL,EUR,GBP`) → monta `USD-BRL`, `USD-EUR`, … e **não** depende do refresh do catálogo no use case (ainda pode existir warm-up do client de `/available` no startup).
- **fx-processing-service**: `MONGODB_URI`, `KAFKA_BOOTSTRAP_SERVERS`, `FX_KAFKA_DLQ_TOPIC` (opcional).
- **api-gateway**: `MONGODB_URI` (precisa incluir **nome do database** no path, ex. `/fx`, e normalmente `authSource=admin` quando o usuário é criado no `admin`).
- **Kafka topic** (pipeline): `exchange-rate-events` (veja `fx.kafka.topic` no producer e a subscription no consumer).
- **MongoDB URI** no compose local: `mongodb://mongodb:27017/fx`.
- **Read API** (`/rates`, `/rates/all`): cada item inclui **`rate`** (atual) e **`previousRate`** (valor que era o atual no write anterior daquele par; `null` no primeiro write ou docs legados).

## Deploy (Easypanel + GitHub Actions)

- Cada serviço pode ser um app separado no Easypanel (build via Dockerfile na pasta do módulo).
- **Kafka** deve ser exposto como **TCP :9092** (não use “Domínio HTTP” pra broker).
- **MongoDB**: use hostname interno do serviço (ex. `fx-event-platform_mongo`) e URI com database + auth.
- O workflow `.github/workflows/deploy.yml` dispara webhooks do Easypanel via secrets:
  - `EASY_PANEL_URL_API_GATEWAY`
  - `EASY_PANEL_URL_EXCHANGE_RATE_SERVICE`
  - `EASY_PANEL_URL_FX_PROCESSING_SERVICE`

## Build and test (per module)

Repositório inclui **Maven Wrapper** (`mvnw` + `.mvn/`) para builds reproduzíveis nos Dockerfiles.

```bash
cd api-gateway && ./mvnw verify
cd ../exchange-rate-service && ./mvnw verify
cd ../fx-processing-service && ./mvnw verify
```

`verify` runs tests and JaCoCo line-coverage checks (where configured in each `pom.xml`).

## Documentation

- [Architecture overview](docs/architecture.md) — context, data flow, layering, and integration points.