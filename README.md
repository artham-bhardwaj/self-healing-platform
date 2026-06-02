# Self-Healing Platform

A Spring Boot 3.5.0 microservices platform with autonomous health monitoring, self-healing capabilities, and comprehensive observability through metrics collection and health history tracking.

## 🎯 Overview

The Self-Healing Platform is designed to provide autonomous health monitoring and recovery mechanisms for Spring Boot applications. It automatically detects degraded states and triggers recovery strategies without manual intervention.

### Key Features

- **Autonomous Health Monitoring** - Real-time system health state management
- **Self-Healing Capabilities** - Automatic recovery strategies for degraded states
- **Health History Tracking** - Complete audit trail of health events and recoveries
- **Custom Metrics** - Prometheus-compatible metrics for recovery and degradation events
- **Spring Boot Actuator Integration** - Full health endpoint support with component details
- **Docker Ready** - Complete containerization with Prometheus and Grafana stack
- **Production-Grade** - Condition-based auto-configuration for safe bean management

## 📋 Prerequisites

- **Java 21** (OpenJDK or equivalent)
- **Maven 3.9+** or use included `mvnw`
- **Docker & Docker Compose** (for container deployment)
- **Git** (for version control)

## 🏗️ Project Structure

```
self-healing-platform/
├── self-healing-starter/        # Auto-configuration library for self-healing features
│   ├── src/main/java/
│   │   └── com/youorg/selfhealing/
│   │       ├── autoconfig/      # Spring Boot auto-configuration
│   │       ├── controller/      # REST endpoints (HistoryController, MetricsController)
│   │       ├── health/          # Health state management
│   │       ├── history/         # Health event history tracking
│   │       ├── lifecycle/       # Startup listeners
│   │       ├── metrics/         # Custom metrics and Prometheus integration
│   │       ├── recovery/        # Recovery strategies
│   │       └── simulation/      # Health degradation simulation
│   └── src/main/resources/
│       └── META-INF/spring/     # Spring Boot auto-configuration discovery
│
├── sample-application/          # Spring Boot application using the starter
│   ├── src/main/java/
│   │   └── com/youorg/sampleapp/
│   │       ├── SampleApplication.java
│   │       └── controller/
│   │           └── HelloController.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── Dockerfile               # Container image definition
│   └── pom.xml
│
├── docker-compose.yml           # Multi-container orchestration
├── monitoring/
│   └── prometheus.yml           # Prometheus scrape configuration
└── pom.xml                      # Parent POM with BOM management
```

## 🚀 Quick Start

### Local Development

#### 1. Clone and Build

```bash
git clone <repository-url>
cd self-healing-platform
mvn clean install
```

#### 2. Run Sample Application

```bash
cd sample-application
mvn spring-boot:run
```

The application starts on `http://localhost:8080`

#### 3. Test Endpoints

```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Health history (returns empty initially)
curl http://localhost:8080/self-healing/history

# Custom metrics
curl http://localhost:8080/metrics/system

# Prometheus metrics (format)
curl http://localhost:8080/actuator/prometheus
```

### Docker Deployment

#### 1. Build and Run Full Stack

```bash
# Navigate to project root
cd self-healing-platform

# Build and start all services (with verbose output)
docker compose up --build

# Or detached mode
docker compose up -d --build
```

#### 2. Access Services

```
Application:  http://localhost:8080
Prometheus:   http://localhost:9090
Grafana:      http://localhost:3000 (admin/admin)
```

#### 3. View Application Logs

```bash
# Follow application logs
docker logs -f self-healing-app

# View specific logs
docker logs self-healing-app

# Stop all containers
docker compose down

# Teardown including volumes
docker compose down -v
```

## 📡 API Endpoints

### Health Endpoints

| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `/actuator/health` | GET | Overall application health | `{"status":"UP","components":{...}}` |
| `/actuator/health/{component}` | GET | Specific component health | Component-specific details |
| `/actuator/prometheus` | GET | Prometheus metrics format | Prometheus text format |

### Self-Healing Endpoints

| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `/self-healing/history` | GET | Complete health history | `[{"timestamp":"...","state":"UP",...}]` |
| `/metrics/system` | GET | System recovery metrics | `{"totalRecoveries":0,"lastRecoveryTime":null,...}` |

### Application Endpoints

| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `/` | GET | Hello endpoint | `"Hello, World!"` |

## ⚙️ Configuration

### application.properties (sample-application)

```properties
# Application name
spring.application.name=sample-application

# Health endpoint configuration
management.endpoints.web.exposure.include=health,prometheus
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always
management.endpoint.health.cache.time-to-live=0

# Custom health status order
management.health.status.order=DOWN,OUT_OF_SERVICE,DEGRADED,UP
```

### prometheus.yml (monitoring)

Prometheus is configured to scrape metrics from:
- Application actuator endpoint at `http://app:8080/actuator/prometheus`
- Default scrape interval: 15 seconds

Enable Prometheus data persistence:

```bash
# Create volume for Prometheus
docker volume create prometheus-data

# Mount in docker-compose.yml:
volumes:
  - prometheus-data:/prometheus
```

## 🔍 Monitoring & Observability

### Prometheus Metrics

Access metrics at: `http://localhost:9090/graph`

Key metrics available:
- `recovered_total` - Total number of recovery events
- `degradations_total` - Total degradation events  
- `last_recovery_time` - Timestamp of last recovery
- `last_degradation_time` - Timestamp of last degradation

### Grafana Dashboards

1. Access Grafana: `http://localhost:3000`
2. Login: `admin` / `admin`
3. Add Prometheus datasource: `http://prometheus:9090`
4. Create dashboards to visualize recovery metrics

### Health Component Details

The application reports detailed health for:
- **diskSpace** - Disk usage and thresholds
- **ping** - PING check
- **selfHealing** - Self-healing platform state
- **ssl** - SSL certificate chain validation

## 🛠️ Development

### Module Details

#### self-healing-starter

A Spring Boot auto-configuration library that provides:

- **AutoConfiguration** - Spring Boot 3 style auto-configuration with conditional bean registration
- **Controllers** - REST endpoints for history and metrics
- **Health Management** - HealthStateManager for state tracking
- **Recovery** - RecoveryManager with pluggable recovery strategies
- **Metrics** - Custom Micrometer metrics integration
- **Simulation** - Health degradation simulation for testing
- **Lifecycle** - StartupListener for initialization

**Key Classes:**
- `SelfHealingAutoConfiguration` - Auto-configuration entry point
- `HistoryController` - Exposes health history API
- `MetricsController` - Exposes custom recovery metrics  
- `HealthHistoryManager` - Tracks health state changes
- `RecoveryManager` - Orchestrates recovery strategies

#### sample-application

A sample Spring Boot application demonstrating:
- Dependency on self-healing-starter
- Standard Spring Boot structure
- REST controller implementation
- Actuator endpoint configuration

### Build and Test

```bash
# Full build with tests
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build specific module
cd self-healing-starter
mvn clean install

# Run tests only
mvn test

# View tests results
cat self-healing-starter/target/surefire-reports/TEST-*.xml
```

### Important: Clean Before Docker Rebuild

```bash
# Ensure JAR has latest code
mvn clean install

# Rebuild Docker image without cache
docker compose down -v
docker compose build --no-cache
docker compose up -d
```

## 🐛 Troubleshooting

### Issue: /self-healing/history returns 404

**Solution:**
1. Verify `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` exists:
   ```bash
   jar tf self-healing-starter/target/self-healing-starter-1.0.0.jar | grep AutoConfiguration.imports
   ```

2. Ensure clean rebuild:
   ```bash
   mvn clean install
   docker compose down -v
   docker compose build --no-cache
   ```

3. Check logs for auto-configuration messages:
   ```bash
   docker logs self-healing-app | grep -i "SELF HEALING\|Configuration"
   ```

### Issue: Application won't start in Docker

**Solution:**
```bash
# Check container logs
docker logs self-healing-app

# Verify JAR exists in image
docker exec self-healing-app jar tf app.jar | grep -i "sample-application"

# Rebuild from scratch
docker compose down -v
docker compose build --no-cache
docker compose up
```

### Issue: Metrics or History endpoints not available

**Solution:**
```bash
# Verify endpoints are exposed in application.properties
management.endpoints.web.exposure.include=health,prometheus

# Check what endpoints are actually registered
curl http://localhost:8080/actuator | jq '._links | keys[]'

# Verify self-healing starter is in classpath
jar tf sample-application/target/sample-application-1.0.0.jar | grep -i "self-healing-starter"
```

## 📝 Building for Production

### 1. Pre-Deployment Checklist

```bash
# Verify clean build
mvn clean install -q

# Check dependency security (requires OWASP plugin)
mvn dependency-check:check

# Run all tests
mvn test

# Verify Docker image builds
docker compose build --no-cache app
```

### 2. Environment Configuration

Create `application-prod.properties`:
```properties
spring.application.name=self-healing-platform-prod
log.level=INFO
management.endpoints.web.exposure.include=health,prometheus
```

### 3. Docker Image Tagging

```bash
# Build with version tag
docker build -t youorg/self-healing-platform:1.0.0 sample-application/

# Tag as latest
docker tag youorg/self-healing-platform:1.0.0 youorg/self-healing-platform:latest

# Push to registry
docker push youorg/self-healing-platform:1.0.0
```

### 4. Deployment Script

```bash
#!/bin/bash
set -e

PROJECT_ROOT="/path/to/self-healing-platform"
cd "$PROJECT_ROOT"

echo "Building project..."
mvn clean install -q

echo "Building Docker image..."
docker compose build --no-cache

echo "Stopping existing containers..."
docker compose down -v

echo "Starting services..."
docker compose up -d

echo "Waiting for application startup..."
sleep 10

echo "Verifying endpoints..."
curl -f http://localhost:8080/actuator/health || exit 1

echo "✓ Deployment successful!"
```

## 🔄 Git Workflow

### Committing Changes

```bash
# Stage changes
git add .

# Commit with descriptive message
git commit -m "Add feature: [description]"

# Push to remote
git push origin main
```

### Common Commits

```bash
# After code changes
git commit -m "Refactor health monitoring logic"

# After README updates
git commit -m "docs: Update deployment instructions"

# After dependency updates
git commit -m "chore: Update Spring Boot to 3.5.0"

# Bug fixes
git commit -m "fix: Resolve 404 on /self-healing/history endpoint"
```

## 📚 Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.5.0 | Application framework |
| Java | 21 | Language/Runtime |
| Maven | 3.9+ | Build automation |
| Docker | Latest | Containerization |
| Prometheus | Latest | Metrics collection |
| Grafana | Latest | Metrics visualization |
| Micrometer | Default | Metrics library |
| Tomcat | 10.1.41 | Servlet container |

## 📄 License

[Specify your license here - e.g., MIT, Apache 2.0]

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make changes and commit: `git commit -m "Add feature"`
3. Push to branch: `git push origin feature/your-feature`
4. Open a Pull Request

## 📞 Support

For issues, questions, or suggestions:
- Check existing documentation in `/docs`
- Review troubleshooting section above
- Check application logs: `docker logs self-healing-app`
- Verify configuration in `application.properties`

## 🎯 Future Enhancements

- [ ] Pluggable recovery strategies
- [ ] Health degradation simulation via REST API
- [ ] Advanced Grafana dashboards
- [ ] Distributed tracing with Spring Cloud Sleuth
- [ ] Health event WebSocket notifications
- [ ] Custom health indicators
- [ ] Circuit breaker integration
- [ ] Kubernetes deployment manifests

---

**Last Updated:** June 2, 2026  
**Version:** 1.0.0  
**Status:** Production Ready
