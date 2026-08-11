package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.incident.Incident;
import com.youorg.selfhealing.incident.IncidentManager;
import com.youorg.selfhealing.metrics.MetricsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/self-healing")
public class RecoveryController {

    private final RecoveryEngine recoveryEngine;
    private final IncidentManager incidentManager;
    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;

    public RecoveryController(RecoveryEngine recoveryEngine, IncidentManager incidentManager, HealthStateManager healthStateManager, MetricsManager metricsManager) {
        this.recoveryEngine = recoveryEngine;
        this.incidentManager = incidentManager;
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
    }

    @GetMapping("/recovery/status")
    public Map<String, Object> getStatus() {
        List<Incident> active = incidentManager.getActiveIncidents();
        return Map.of(
                "healthState", healthStateManager.getCurrentState(),
                "activeIncidents", active.size(),
                "totalRecoveries", metricsManager.getTotalRecoveries(),
                "totalDegradations", metricsManager.getTotalDegradations()
        );
    }

    @GetMapping("/recovery/trigger")
    public String triggerRecovery(@RequestParam long incidentId) {
        Incident incident = incidentManager.getIncident(incidentId);
        if (incident == null) {
            return "Incident not found";
        }

        recoveryEngine.attemptRecovery(incident);
        return "Recovery started for incident " + incidentId;
    }

    @GetMapping("/recovery/reset")
    public String resetSystem() {
        healthStateManager.setCurrentState(HealthState.UP);
        incidentManager.getIncidentHistory().forEach(existing -> incidentManager.markResolved(existing, "System manually reset"));
        return "Self-healing platform reset to healthy state";
    }
}
