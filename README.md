# 🚀 Self-Healing Platform

> A production-ready Spring Boot platform that detects degraded system states, triggers automated recovery workflows, tracks health events, and provides observability through Prometheus and Grafana.

🌐 **Live Demo:** https://self-healing-platform.onrender.com

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![Prometheus](https://img.shields.io/badge/Prometheus-Monitoring-orange)
![Grafana](https://img.shields.io/badge/Grafana-Dashboard-yellow)
![Render](https://img.shields.io/badge/Render-Deployed-purple)

---

# 📸 Screenshots

## Grafana Monitoring Dashboard

Real-time visualization of application health, degradation events, and recovery operations.

### Dashboard Overview

![Grafana Dashboard](screenshots/grafana-1.png)

### Recovery Monitoring

![Grafana Recovery Dashboard](screenshots/grafana-2.png)

---

## Health Endpoints

Application health exposed through Spring Boot Actuator and custom health indicators.

![Health Endpoint](screenshots/health-endpoint1.png.png)

![Health Endpoint Details](screenshots/health-endpoint2.png)

---

## Health Event History API

Tracks all health state transitions and automated recovery events for auditability and operational diagnostics.

![Health History API](screenshots/history-api.png)

---

## Custom Prometheus Metrics

Custom Micrometer metrics exposed for Prometheus scraping and Grafana visualization.

![Prometheus Metrics](screenshots/prometheus-metrics.png)

---

## Dockerized Monitoring Stack

Spring Boot application, Prometheus, and Grafana running together in a containerized environment.

![Docker Stack](screenshots/docker-stack.png.png)

---

## Cloud Deployment

Live deployment hosted on Render.

![Render Deployment](screenshots/render-deployment.png.png)

---