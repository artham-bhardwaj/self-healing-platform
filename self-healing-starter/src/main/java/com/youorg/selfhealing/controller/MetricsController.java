package com.youorg.selfhealing.controller;

import com.youorg.selfhealing.metrics.MetricsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MetricsController {
    
    private final MetricsManager metricsManager;

    public MetricsController(MetricsManager metricsManager) {
        this.metricsManager = metricsManager;
    }

    @GetMapping("/metrics/system")
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalDegradations", metricsManager.getTotalDegradations());
        metrics.put("totalRecoveries", metricsManager.getTotalRecoveries());
        metrics.put("lastDegradationTime", metricsManager.getLastDegradationTime());
        metrics.put("lastRecoveryTime", metricsManager.getLastRecoveryTime());
        return metrics;
    }
}
        