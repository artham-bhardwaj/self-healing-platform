package com.youorg.selfhealing.metrics;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import jakarta.annotation.PostConstruct;

public class MetricsConfiguration {

    private final MeterRegistry meterRegistry;
    private final MetricsManager metricsManager;
    private final HealthStateManager healthStateManager;

    public MetricsConfiguration(
            MeterRegistry meterRegistry,
            MetricsManager metricsManager,
            HealthStateManager healthStateManager
    ) {
        this.meterRegistry = meterRegistry;
        this.metricsManager = metricsManager;
        this.healthStateManager = healthStateManager;
    }

    @PostConstruct
    public void registerMetrics() {

        Gauge.builder(
                "self_healing_degradation_total",
                metricsManager,
                MetricsManager::getTotalDegradations
        ).register(meterRegistry);

        Gauge.builder(
                "self_healing_recovery_total",
                metricsManager,
                MetricsManager::getTotalRecoveries
        ).register(meterRegistry);

        Gauge.builder(
                "self_healing_current_state",
                healthStateManager,
                manager -> manager.getCurrentState() == HealthState.DEGRADED ? 1 : 0
        ).register(meterRegistry);
    }
}