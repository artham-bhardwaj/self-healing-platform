package com.youorg.selfhealing.incident;

import com.youorg.selfhealing.health.HealthState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Incident {

    private final long id;
    private final String component;
    private final IncidentType type;
    private final String summary;
    private final String description;
    private final HealthState detectedState;
    private final Instant createdAt;
    private final List<String> timeline = new ArrayList<>();
    private IncidentStatus status;
    private String severity;
    private Instant lastUpdatedAt;
    private Instant resolvedAt;
    private int recoveryAttempts;

    public Incident(
            long id,
            String component,
            IncidentType type,
            String summary,
            String description,
            HealthState detectedState
    ) {
        this.id = id;
        this.component = component;
        this.type = type;
        this.summary = summary;
        this.description = description;
        this.detectedState = detectedState;
        this.createdAt = Instant.now();
        this.lastUpdatedAt = createdAt;
        this.status = IncidentStatus.DETECTED;
        this.severity = determineSeverity(type);
        this.timeline.add("DETECTED :: " + summary);
    }

    public long getId() {
        return id;
    }

    public String getComponent() {
        return component;
    }

    public IncidentType getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public HealthState getDetectedState() {
        return detectedState;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
        this.lastUpdatedAt = Instant.now();
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public int getRecoveryAttempts() {
        return recoveryAttempts;
    }

    public void incrementRecoveryAttempts() {
        this.recoveryAttempts++;
        this.lastUpdatedAt = Instant.now();
        this.timeline.add("RECOVERY_ATTEMPT " + recoveryAttempts + " :: automated recovery triggered");
    }

    public List<String> getTimeline() {
        return List.copyOf(timeline);
    }

    public void addTimeline(String event) {
        this.timeline.add(event);
        this.lastUpdatedAt = Instant.now();
    }

    public boolean isActive() {
        return status != IncidentStatus.RESOLVED && status != IncidentStatus.ESCALATED;
    }

    public String getIncidentKey() {
        return component + ":" + type.name();
    }

    private String determineSeverity(IncidentType type) {
        return switch (type) {
            case CPU_SPIKE, MEMORY_PRESSURE -> "MEDIUM";
            case DEPENDENCY_FAILURE, LATENCY_SPIKE -> "HIGH";
            case RECOVERY_FAILURE -> "CRITICAL";
            default -> "LOW";
        };
    }
}
