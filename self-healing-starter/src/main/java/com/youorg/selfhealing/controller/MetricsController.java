package com.youorg.selfhealing.controller;

import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.metrics.MetricsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsManager metricsManager;
    private final HealthStateManager healthStateManager;

    public MetricsController(MetricsManager metricsManager, HealthStateManager healthStateManager) {
        this.metricsManager = metricsManager;
        this.healthStateManager = healthStateManager;
    }

    @GetMapping("/metrics/system")
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        int totalDegradations = metricsManager.getTotalDegradations();
        int totalRecoveries = metricsManager.getTotalRecoveries();
        int totalRecoveryFailures = metricsManager.getTotalRecoveryFailures();
        double successRate = totalDegradations == 0 ? 100 : ((double) totalRecoveries / (double) totalDegradations) * 100;

        metrics.put("totalDegradations", totalDegradations);
        metrics.put("totalRecoveries", totalRecoveries);
        metrics.put("totalRecoveryFailures", totalRecoveryFailures);
        metrics.put("activeIncidents", metricsManager.getActiveIncidents());
        metrics.put("lastDegradationTime", metricsManager.getLastDegradationTime());
        metrics.put("lastRecoveryTime", metricsManager.getLastRecoveryTime());
        metrics.put("lastRecoveryFailureTime", metricsManager.getLastRecoveryFailureTime());
        metrics.put("lastFailureType", metricsManager.getLastFailureType());
        metrics.put("successRate", Math.round(successRate));
        metrics.put("healthState", healthStateManager.getCurrentState().name());
        metrics.put("statusReason", healthStateManager.getStatusReason());
        return metrics;
    }
}
        