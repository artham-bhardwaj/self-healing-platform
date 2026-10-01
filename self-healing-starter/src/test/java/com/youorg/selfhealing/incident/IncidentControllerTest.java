package com.youorg.selfhealing.incident;

import com.youorg.selfhealing.health.HealthState;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentControllerTest {

    @Test
    void summaryCountsOnlyResolvedIncidentsAndKeepsEscalatedSeparate() {
        IncidentManager incidentManager = new IncidentManager();
        Incident resolved = incidentManager.createIncident(
                "payments-api",
                IncidentType.CPU_SPIKE,
                "CPU usage elevated",
                "CPU usage exceeded threshold",
                HealthState.DEGRADED
        );
        incidentManager.markResolved(resolved, "CPU usage returned to normal");

        Incident escalated = incidentManager.createIncident(
                "payments-api",
                IncidentType.DEPENDENCY_FAILURE,
                "Payment dependency unavailable",
                "The payment dependency is not responding",
                HealthState.DOWN
        );
        incidentManager.markEscalated(escalated, "Automatic recovery exhausted");

        Incident activeCritical = incidentManager.createIncident(
                "payments-api",
                IncidentType.RECOVERY_FAILURE,
                "Recovery failed",
                "The recovery action did not restore service",
                HealthState.DOWN
        );

        Map<String, Object> summary = new IncidentController(incidentManager).getSummary();

        assertThat(summary)
                .containsEntry("totalIncidents", 3)
                .containsEntry("activeIncidents", 1)
                .containsEntry("resolvedIncidents", 1L)
                .containsEntry("criticalActiveIncidents", 1L);
    }
}
