package com.youorg.selfhealing.incident;

import com.youorg.selfhealing.health.HealthState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class IncidentManager {

    private final Map<String, Incident> activeIncidentByKey = new ConcurrentHashMap<>();
    private final List<Incident> allIncidents = new ArrayList<>();
    private final AtomicLong incidentSequence = new AtomicLong(1L);

    public synchronized Incident createIncident(
            String component,
            IncidentType type,
            String summary,
            String description,
            HealthState detectedState
    ) {
        String incidentKey = component + ":" + type.name();
        Incident existing = activeIncidentByKey.get(incidentKey);
        if (existing != null) {
            existing.addTimeline("DUPLICATE_DETECTED :: incident already tracked for component and type");
            return existing;
        }

        Incident incident = new Incident(
                incidentSequence.getAndIncrement(),
                component,
                type,
                summary,
                description,
                detectedState
        );
        activeIncidentByKey.put(incidentKey, incident);
        allIncidents.add(incident);
        return incident;
    }

    public synchronized List<Incident> getActiveIncidents() {
        return activeIncidentByKey.values().stream()
                .filter(Objects::nonNull)
                .filter(Incident::isActive)
                .toList();
    }

    public synchronized List<Incident> getIncidentHistory() {
        return List.copyOf(allIncidents);
    }

    public synchronized Incident getIncident(long id) {
        return allIncidents.stream()
                .filter(incident -> incident.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void markRecovering(Incident incident, String message) {
        if (incident == null) {
            return;
        }
        incident.setStatus(IncidentStatus.RECOVERING);
        incident.incrementRecoveryAttempts();
        incident.addTimeline("RECOVERY_STARTED :: " + message);
    }

    public synchronized void markRecovered(Incident incident, String message) {
        if (incident == null) {
            return;
        }
        activeIncidentByKey.remove(incident.getIncidentKey());
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setResolvedAt(Instant.now());
        incident.addTimeline("RECOVERY_SUCCEEDED :: " + message);
    }

    public synchronized void markRecoveryFailed(Incident incident, String message) {
        if (incident == null) {
            return;
        }
        incident.setStatus(IncidentStatus.RECOVERY_FAILED);
        incident.addTimeline("RECOVERY_FAILED :: " + message);
    }

    public synchronized void markEscalated(Incident incident, String message) {
        if (incident == null) {
            return;
        }
        activeIncidentByKey.remove(incident.getIncidentKey());
        incident.setStatus(IncidentStatus.ESCALATED);
        incident.addTimeline("ESCALATED :: " + message);
    }

    public synchronized void markResolved(Incident incident, String message) {
        if (incident == null) {
            return;
        }
        activeIncidentByKey.remove(incident.getIncidentKey());
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setResolvedAt(Instant.now());
        incident.addTimeline("RESOLVED :: " + message);
    }
}
