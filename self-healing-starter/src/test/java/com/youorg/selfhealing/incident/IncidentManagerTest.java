package com.youorg.selfhealing.incident;

import com.youorg.selfhealing.health.HealthState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentManagerTest {

    @Test
    void createsUniqueActiveIncidentAndResolvesCorrectly() {
        IncidentManager incidentManager = new IncidentManager();

        Incident first = incidentManager.createIncident(
                "payments-api",
                IncidentType.DEPENDENCY_FAILURE,
                "Downstream cache unavailable",
                "The cache dependency is timing out",
                HealthState.DEGRADED
        );

        Incident duplicate = incidentManager.createIncident(
                "payments-api",
                IncidentType.DEPENDENCY_FAILURE,
                "Downstream cache unavailable",
                "The cache dependency is timing out",
                HealthState.DEGRADED
        );

        assertThat(first.getId()).isEqualTo(duplicate.getId());
        assertThat(incidentManager.getActiveIncidents()).hasSize(1);

        incidentManager.markResolved(first, "Dependency recovered after retry");

        assertThat(incidentManager.getActiveIncidents()).isEmpty();
        assertThat(first.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }
}
