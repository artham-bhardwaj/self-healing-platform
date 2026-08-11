package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.incident.Incident;

public interface RecoveryStrategy {

    String getName();

    boolean supports(Incident incident);

    HealthState execute(Incident incident);
}
