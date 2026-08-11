package com.youorg.selfhealing.health;

import java.time.LocalDateTime;

public class HealthStateManager {

    private HealthState currentState = HealthState.UP;
    private String statusReason = "system initialized";
    private LocalDateTime lastUpdatedAt = LocalDateTime.now();

    public HealthState getCurrentState() {
        return currentState;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public synchronized void setCurrentState(HealthState currentState) {
        setCurrentState(currentState, currentState.name());
    }

    public synchronized void setCurrentState(HealthState currentState, String statusReason) {
        this.currentState = currentState;
        this.statusReason = statusReason;
        this.lastUpdatedAt = LocalDateTime.now();
    }

}