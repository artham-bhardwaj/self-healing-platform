# Self-Healing Platform

A production-ready Spring Boot 3.5.0 self-healing platform with autonomous health monitoring, automated recovery workflows, and health event history tracking. Designed for resilience and observability.

**🌐 Live Demo:** https://self-healing-platform.onrender.com

## 🎯 Overview

The Self-Healing Platform provides autonomous health monitoring and recovery mechanisms for Spring Boot applications. It automatically detects degraded states, triggers recovery strategies, and maintains a complete audit trail of health events—all without manual intervention.

### Key Features

- ✅ **Autonomous Health Monitoring** - Real-time system health state management
- ✅ **Self-Healing Capabilities** - Automatic recovery strategies for degraded states  
- ✅ **Health History Tracking** - Complete audit trail of health events and recoveries
- ✅ **API Simulation** - Trigger CPU/memory degradation for testing recovery workflows
- ✅ **Spring Boot Actuator** - Full health endpoint support with component details
- ✅ **Custom Metrics** - Prometheus-compatible metrics for recovery events (optional)
- ✅ **Production-Grade** - Condition-based auto-configuration, Docker-ready, Render-deployed

## 📋 Prerequisites

- **Java 21** (OpenJDK or equivalent)
- **Maven 3.9+** or use included `mvnw`
- **Docker & Docker Compose** (for local development)
- **Git** (for version control)

## 🚀 Deployment

### 🌟 Production: Render (Free Tier) - Recommended

**The easiest way to deploy:**

1. **Push to GitHub** ✅ (Already done)
2. **Connect Render:** Visit `https://dashboard.render.com`
3. **Import Repository:** Select `self-healing-platform`
4. **Deploy:** Render automatically reads `render.yaml` and deploys
5. **Access:** Your app runs at `https://self-healing-platform.onrender.com`

**Test your deployment:**
```bash
curl https://self-healing-platform.onrender.com/actuator/health
curl https://self-healing-platform.onrender.com/self-healing/history
curl -X POST https://self-healing-platform.onrender.com/simulate/memory
```

### Local Development

#### 1. Clone and Build

```bash
git clone https://github.com/artham-bhardwaj/self-healing-platform.git
cd self-healing-platform
mvn clean install
```

#### 2. Run Application

```bash
cd sample-application
mvn spring-boot:run
```

Access: `http://localhost:8080`

#### 3. Test Endpoints

```bash
# Health status
curl http://localhost:8080/actuator/health

# Health history
curl http://localhost:8080/self-healing/history

# System metrics
curl http://localhost:8080/metrics/system

# Simulate memory degradation
curl -X POST http://localhost:8080/simulate/memory

# Simulate CPU degradation  
curl -X POST http://localhost:8080/simulate/cpu
```

### Local Docker Deployment

```bash
# Build and run with Docker Compose (includes Prometheus + Grafana)
docker compose up --build

# Access application at http://localhost:8080
# Optional monitoring:
#   - Prometheus:  http://localhost:9090
#   - Grafana:     http://localhost:3000 (admin/admin)

# Stop all services
docker compose down -v
```

## 📡 API Endpoints

### Health & Status

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/actuator/health` | GET | Overall application health |
| `/actuator/health/{component}` | GET | Specific component health |
| `/actuator/prometheus` | GET | Prometheus metrics format |

### Self-Healing API

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/self-healing/history` | GET | Complete health event history |
| `/metrics/system` | GET | Recovery and degradation metrics |

### Health Simulation (Testing)

| Endpoint | Method | Description | Effect |
|----------|--------|-------------|--------|
| `/simulate/memory` | POST | Trigger memory pressure | Sets state to DEGRADED → auto-recovers |
| `/simulate/cpu` | POST | Trigger CPU pressure | Sets state to DEGRADED → auto-recovers |

### Application

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Welcome endpoint |

## 📊 Optional: Local Monitoring Stack

For development and testing observability:

```bash
# Start with Prometheus + Grafana (included in docker-compose.yml)
docker compose up --build

# Access services:
#   - App:        http://localhost:8080
#   - Prometheus: http://localhost:9090
#   - Grafana:    http://localhost:3000 (admin/admin)
```

**Key Metrics Available:**
- `recovered_total` - Total recovery events
- `degradations_total` - Total degradation events
- `last_recovery_time` - Last recovery timestamp
- `last_degradation_time` - Last degradation timestamp

See [screenshots/](/screenshots/) for example Grafana dashboards.

## 📝 Resume Bullet Points

Use these on your resume or LinkedIn:

> **Self-Healing Platform** | Spring Boot 3.5, Docker, Render  
> - Developed a custom Spring Boot starter library with conditional auto-configuration for resilience monitoring and automated recovery workflows
> - Implemented CPU and memory degradation simulation APIs for testing self-healing mechanisms
> - Built custom Prometheus metrics and visualization dashboards for operational observability
> - Exposed health event history APIs (`/self-healing/history`) for system diagnostics and health tracking
> - Containerized platform using Docker and deployed to Render with automatic health checks
> - **Live Demo:** https://self-healing-platform.onrender.com

## 🛠️ Development

### Project Structure

```
self-healing-platform/
├── self-healing-starter/        # Custom Spring Boot library
│   ├── autoconfig/              # Spring Boot auto-configuration
│   ├── controller/              # REST endpoints
│   ├── health/                  # HealthStateManager
│   ├── history/                 # Health event history tracking
│   ├── metrics/                 # Custom Prometheus metrics
│   ├── recovery/                # Recovery orchestration
│   └── simulation/              # Degradation simulation
│
├── sample-application/          # Sample Spring Boot app
│   ├── SampleApplication.java
│   ├── HelloController.java
│   └── Dockerfile
│
├── docker-compose.yml           # Local dev stack
├── render.yaml                  # Render deployment config
├── monitoring/                  # Prometheus config
└── pom.xml                      # Parent POM
```

### Quick Build

```bash
# Full build with tests
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Run application locally
mvn spring-boot:run -pl sample-application
```

### Important: Clean Rebuild Before Deploy

```bash
mvn clean install               # Build everything fresh
docker compose down -v          # Remove old containers
docker compose build --no-cache # Fresh Docker build
docker compose up -d            # Start services
```

## 🚀 Roadmap: Version 2.0 (Coming Next)

### Automatic Resource Monitoring (Major Feature)

Currently (v1.0):
- Manual simulation: `/simulate/memory`, `/simulate/cpu`
- API-driven health state degradation

**Version 2.0 will add:**

A new `ResourceMonitor` component that:
- ✅ Continuously monitors Heap % and CPU %
- ✅ Checks metrics every 5 seconds
- ✅ Automatically sets state to `DEGRADED` when thresholds exceeded:
  - Heap > 80%
  - CPU > 75%
- ✅ Automatically recovers when metrics normalize
- ✅ No manual intervention required

**Impact:** Transforms from a *simulation platform* to a *real self-monitoring platform*

```java
// Pseudo-code for v2.0
@Component
public class ResourceMonitor {
    
    @Scheduled(fixedRate = 5000)
    public void checkResources() {
        long heapUsedPercent = getHeapPercentage();
        long cpuPercent = getCPUPercentage();
        
        if (heapUsedPercent > 80 || cpuPercent > 75) {
            healthStateManager.setCurrentState(DEGRADED);
        } else {
            healthStateManager.setCurrentState(UP);
        }
    }
}
```

### Other v2.0 Enhancements
- [ ] WebSocket notifications for health state changes
- [ ] Pluggable recovery strategies
- [ ] Distributed tracing with Spring Cloud Sleuth
- [ ] Kubernetes deployment manifests (Helm charts)
- [ ] Custom health indicators for business metrics

## 🛠️ Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.5.0 | Application framework |
| Java | 21 | Language/Runtime |
| Maven | 3.9+ | Build automation |
| Docker | Latest | Containerization |
| Prometheus | Latest | Metrics collection (optional) |
| Grafana | Latest | Visualization (optional) |
| Micrometer | Default | Metrics library |

## 🔄 Git Workflow

```bash
# Stage changes
git add .

# Commit with descriptive message
git commit -m "feat: Add v1.0 production deployment"

# Push to remote
git push origin main
```

## 📞 Support & Troubleshooting

### Issue: Endpoints return 404

**Solution:**
```bash
# Ensure clean rebuild
mvn clean install

# Verify startup message
docker logs self-healing-app | grep "SELF HEALING"

# Check health component registration
curl http://localhost:8080/actuator | jq '.components'
```

### Issue: Application won't start in Docker

**Solution:**
```bash
# Check logs
docker logs self-healing-app

# Rebuild completely
docker compose down -v
docker compose build --no-cache
docker compose up
```

### Issue: Metrics not appearing

**Solution:**
```bash
# Verify actuator endpoints are exposed
# Check: management.endpoints.web.exposure.include=health,prometheus

# Check available endpoints
curl http://localhost:8080/actuator | jq '._links | keys[]'
```

## 📄 License

MIT License

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m "feat: Add feature"`
3. Push branch: `git push origin feature/your-feature`
4. Open Pull Request

---

**Status:** ✅ v1.0 Production Ready  
**Deployed:** Render (https://self-healing-platform.onrender.com)  
**Last Updated:** June 2, 2026  
**Next:** v2.0 with Automatic Resource Monitoring
