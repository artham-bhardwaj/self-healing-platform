# SentinelX — Self-Healing Platform

SentinelX is a Java 21 and Spring Boot 3.5 resilience-engineering prototype. It demonstrates the operational lifecycle of a self-healing service: detect degradation, create and track an incident, apply a recovery strategy, verify the outcome, and expose the result through health, metrics, APIs, and a live operations console.

## Highlights

- Detects and records degraded health states.
- Creates incidents with lifecycle data, severity, timestamps, and event timelines.
- Deduplicates active incidents by component and incident type.
- Applies strategy-based automated recovery, including dependency-aware recovery.
- Provides CPU, memory, dependency-outage, and latency-spike simulations.
- Exposes Spring Boot Actuator health and Prometheus-compatible metrics.
- Includes a responsive operations console and a prototype-overview page.

## Architecture

```text
Browser
  └─ Operations Console
       └─ Sample Application (Spring Boot)
            ├─ Actuator health and Prometheus endpoint
            └─ Self-Healing Starter
                 ├─ HealthStateManager
                 ├─ IncidentManager
                 ├─ RecoveryEngine and recovery strategies
                 ├─ MetricsManager
                 ├─ HealthHistoryManager
                 └─ Simulation and REST controllers
```

## Incident lifecycle

```text
Detect → Create incident → Recover → Verify → Resolve / Escalate
```

The application keeps an in-memory history of incidents and health events for the current runtime. A simulation marks the system as degraded, creates or reuses the matching active incident, attempts recovery asynchronously, then records the resulting health transition.

## Technology

- Java 21
- Spring Boot 3.5
- Spring Boot Actuator
- Micrometer / Prometheus
- Maven
- HTML, CSS, JavaScript, and Chart.js
- Docker Compose, Prometheus, and Grafana

## Project structure

```text
.
├── sample-application/       # Spring Boot application and web console
├── self-healing-starter/     # Reusable self-healing library
├── monitoring/prometheus.yml # Prometheus scrape configuration
├── docker-compose.yml        # App, Prometheus, and Grafana stack
├── render.yaml               # Render deployment definition
└── pom.xml                   # Multi-module Maven build
```

## Run locally

### Prerequisites

- JDK 21
- Maven 3.9+
- Docker and Docker Compose (optional, for the monitoring stack)

### Start the application

From the repository root:

```bash
mvn clean install
mvn -pl sample-application spring-boot:run
```

Open the application at:

- Operations console: [http://localhost:8080/](http://localhost:8080/)
- Prototype overview: [http://localhost:8080/prototype-overview.html](http://localhost:8080/prototype-overview.html)
- Health details: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

### Start with Docker Compose

The application Docker image uses the JAR produced by Maven, so build the project first:

```bash
mvn clean package
docker compose up --build -d
```

Services are available at:

| Service | URL | Default credentials |
| --- | --- | --- |
| SentinelX application | [http://localhost:8080](http://localhost:8080) | — |
| Prometheus | [http://localhost:9090](http://localhost:9090) | — |
| Grafana | [http://localhost:3000](http://localhost:3000) | `admin` / `admin` |

Stop the stack with:

```bash
docker compose down
```

## API reference

### Health and observability

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/actuator/health` | Actuator health, including self-healing state. |
| GET | `/actuator/prometheus` | Prometheus-format metrics. |
| GET | `/metrics/system` | Application health and recovery summary. |

### Incidents and recovery

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/self-healing/incidents` | All tracked incidents. |
| GET | `/self-healing/incidents/active` | Incidents that are still active. |
| GET | `/self-healing/incidents/summary` | Incident counts and critical-active count. |
| GET | `/self-healing/incidents/{id}` | One incident by ID. |
| GET | `/self-healing/history` | Health-state transition history. |
| GET | `/self-healing/recovery/status` | Current recovery and incident status. |
| GET | `/self-healing/recovery/trigger?incidentId={id}` | Manually attempt recovery for an incident. |
| GET | `/self-healing/recovery/reset` | Reset health and resolve tracked incidents. |

### Failure simulations

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/self-healing/simulate` | List supported scenarios. |
| GET | `/self-healing/simulate/trigger?scenarioName={name}` | Start a simulation and automated recovery. |

Supported scenario names: `cpu-spike`, `memory-pressure`, `dependency-failure`, and `latency-spike`.

Example:

```bash
curl 'http://localhost:8080/self-healing/simulate/trigger?scenarioName=cpu-spike'
```

## Operations console

The console gives an operational view of the current in-memory runtime state:

- System health, active incidents, recovery rate, and MTTR
- One-click failure simulations
- Degradation and recovery KPI totals
- Incident trend chart and operational summary
- Selectable incident history with lifecycle details
- Health-event recovery timeline

## Test

Run the full multi-module test suite:

```bash
mvn test
```

## Current scope

This is a portfolio-grade prototype, not a production incident-management service. Incident and event data is intentionally in memory and is reset when the application restarts. Natural next steps include persistent storage, alert routing, authenticated access, retry policy controls, distributed-service topology, and production deployment policies.

## License

MIT License. You are welcome to fork, learn from, and extend the project.
