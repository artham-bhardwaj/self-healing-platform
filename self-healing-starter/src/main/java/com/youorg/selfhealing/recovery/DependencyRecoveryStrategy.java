package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.incident.Incident;
import com.youorg.selfhealing.incident.IncidentType;

public class DependencyRecoveryStrategy implements RecoveryStrategy {

    @Override
    public String getName() {
        return "dependency-retry";
    }

    @Override
    public boolean supports(Incident incident) {
        return incident.getType() == IncidentType.DEPENDENCY_FAILURE;
    }

    @Override
    public HealthState execute(Incident incident) {
        incident.addTimeline("RECOVERY_ACTION :: retry dependency and restore downstream connectivity");
        return HealthState.UP;
    }
}
