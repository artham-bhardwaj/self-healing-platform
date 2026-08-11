package com.youorg.selfhealing.metrics;

import java.time.LocalDateTime;

public class MetricsManager {

    private int totalDegradations = 0;
    private int totalRecoveries = 0;
    private int totalRecoveryFailures = 0;
    private int activeIncidents = 0;

    private LocalDateTime lastDegradationTime;
    private LocalDateTime lastRecoveryTime;
    private LocalDateTime lastRecoveryFailureTime;
    private String lastFailureType;

    public synchronized void recordDegradation() {
        totalDegradations++;
        lastDegradationTime = LocalDateTime.now();
        activeIncidents = Math.max(1, activeIncidents + 1);
        System.out.println("Degradation recorded. Total degradations: " + totalDegradations);
    }

    public synchronized void recordRecovery() {
        totalRecoveries++;
        lastRecoveryTime = LocalDateTime.now();
        activeIncidents = Math.max(0, activeIncidents - 1);
        System.out.println("Recovery recorded. Total recoveries: " + totalRecoveries);
    }

    public synchronized void recordRecoveryFailure(String failureType) {
        totalRecoveryFailures++;
        lastRecoveryFailureTime = LocalDateTime.now();
        lastFailureType = failureType;
        System.out.println("Recovery failure recorded. Failure type: " + failureType);
    }

    public synchronized void setActiveIncidents(int activeIncidents) {
        this.activeIncidents = activeIncidents;
    }

    public int getTotalDegradations() {
        return totalDegradations;
    }

    public int getTotalRecoveries() {
        return totalRecoveries;
    }

    public int getTotalRecoveryFailures() {
        return totalRecoveryFailures;
    }

    public int getActiveIncidents() {
        return activeIncidents;
    }

    public LocalDateTime getLastDegradationTime() {
        return lastDegradationTime;
    }

    public LocalDateTime getLastRecoveryTime() {
        return lastRecoveryTime;
    }

    public LocalDateTime getLastRecoveryFailureTime() {
        return lastRecoveryFailureTime;
    }

    public String getLastFailureType() {
        return lastFailureType;
    }
}