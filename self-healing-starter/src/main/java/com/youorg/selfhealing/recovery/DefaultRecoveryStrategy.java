package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.incident.Incident;
import com.youorg.selfhealing.incident.IncidentType;

public class DefaultRecoveryStrategy implements RecoveryStrategy {

    private final HealthStateManager healthStateManager;

    public DefaultRecoveryStrategy(HealthStateManager healthStateManager) {
        this.healthStateManager = healthStateManager;
    }

    @Override
    public String getName() {
        return "default-recovery";
    }

    @Override
    public boolean supports(Incident incident) {
        return incident.getType() == IncidentType.CPU_SPIKE
                || incident.getType() == IncidentType.MEMORY_PRESSURE
                || incident.getType() == IncidentType.LATENCY_SPIKE
                || incident.getType() == IncidentType.UNKNOWN;
    }

    @Override
    public HealthState execute(Incident incident) {
        healthStateManager.setCurrentState(HealthState.UP);
        incident.addTimeline("RECOVERY_ACTION :: reset degraded state and resumed service health");
        return HealthState.UP;
    }
}
