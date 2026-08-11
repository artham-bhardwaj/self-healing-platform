package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.incident.IncidentType;

public class FailureScenario {

    private final String name;
    private final IncidentType incidentType;
    private final String component;
    private final String summary;
    private final String description;
    private final HealthState detectedState;
    private final long delayMs;

    public FailureScenario(
            String name,
            IncidentType incidentType,
            String component,
            String summary,
            String description,
            HealthState detectedState,
            long delayMs
    ) {
        this.name = name;
        this.incidentType = incidentType;
        this.component = component;
        this.summary = summary;
        this.description = description;
        this.detectedState = detectedState;
        this.delayMs = delayMs;
    }

    public String getName() {
        return name;
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public String getComponent() {
        return component;
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

    public long getDelayMs() {
        return delayMs;
    }
}
