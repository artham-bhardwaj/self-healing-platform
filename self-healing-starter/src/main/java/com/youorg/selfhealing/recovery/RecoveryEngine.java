package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.incident.Incident;
import com.youorg.selfhealing.incident.IncidentManager;
import com.youorg.selfhealing.incident.IncidentStatus;
import com.youorg.selfhealing.metrics.MetricsManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RecoveryEngine {

    private final List<RecoveryStrategy> strategies = new CopyOnWriteArrayList<>();
    private final IncidentManager incidentManager;
    private final MetricsManager metricsManager;

    public RecoveryEngine(IncidentManager incidentManager, MetricsManager metricsManager, List<RecoveryStrategy> strategies) {
        this.incidentManager = incidentManager;
        this.metricsManager = metricsManager;
        this.strategies.addAll(strategies);
    }

    public HealthState attemptRecovery(Incident incident) {
        if (incident == null) {
            return HealthState.UP;
        }

        incidentManager.markRecovering(incident, "Recovery workflow started");
        RecoveryStrategy strategy = strategies.stream()
                .filter(candidate -> candidate.supports(incident))
                .findFirst()
                .orElse(null);

        if (strategy == null) {
            incidentManager.markRecoveryFailed(incident, "No supported recovery strategy was available");
            return HealthState.DEGRADED;
        }

        incident.addTimeline("RECOVERY_STRATEGY :: " + strategy.getName());
        HealthState result = strategy.execute(incident);
        metricsManager.recordRecovery();

        if (result == HealthState.UP) {
            incidentManager.markRecovered(incident, "Recovery succeeded");
            return HealthState.UP;
        }

        metricsManager.recordRecoveryFailure(incident.getType().name());
        incidentManager.markRecoveryFailed(incident, "Recovery strategy did not restore health");
        return HealthState.DEGRADED;
    }
}
