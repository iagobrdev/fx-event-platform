# FX Event Platform

Event-driven FX rate platform: poll the [AwesomeAPI](https://economia.awesomeapi.com.br) (Brazil), publish changes to **Apache Kafka**, persist snapshots in **MongoDB**, and expose **HTTP APIs** for latest rates and cross-currency conversion.

## Modules

| Module | Role |
|--------|------|
| **exchange-rate-service** | Scheduled fetch of available pairs and last quotes (batched HTTP), in-memory cache, Kafka producer (`exchange-rate-events`). |
| **fx-processing-service** | Kafka consumer; normalizes events and upserts documents in MongoDB collection `exchange_rates`. |
| **api-gateway** | Reads from MongoDB; exposes `/rates` and `/convert`; uses in-memory graph for cross-rate paths. |

Stack: **Java 21**, **Spring Boot 3.x**, **MongoDB**, **Kafka (KRaft)**.

## Prerequisites

- **Docker** and **Docker Compose** (recommended for full stack).
- Or **JDK 21** and **Maven 3.9+** for local JVM runs plus your own Kafka and MongoDB.

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

curl -s -X POST http://localhost:8080/convert \
  -H "Content-Type: application/json" \
  -d '{"from":"USD","to":"BRL","amount":100}'

curl -s http://localhost:8081/exchange-rate/state
```

## Configuration highlights

- **exchange-rate-service**: `fx.poll.api-interval-ms` (default 30s) drives how often the full fetch runs. Pairs and quotes use the AwesomeAPI URLs in `application.yml`. Max pairs and catalog refresh are under `fx.poll.*`.
- **Kafka topic** (all services): `exchange-rate-events` (see `fx.kafka.topic` / consumer subscription).
- **MongoDB URI** for gateway and processing: `MONGODB_URI` (compose sets `mongodb://mongodb:27017/fx`).

## Build and test (per module)

```bash
cd api-gateway && mvn verify
cd ../exchange-rate-service && mvn verify
cd ../fx-processing-service && mvn verify
```

`verify` runs tests and JaCoCo line-coverage checks (where configured in each `pom.xml`).

## Documentation

- [Architecture overview](docs/architecture.md) — context, data flow, layering, and integration points.

