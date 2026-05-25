package com.youorg.selfhealing.metrics;

import java.time.LocalDateTime;

public class MetricsManager {

    private int totalDegradations = 0;
    private int totalRecoveries = 0;

    private LocalDateTime lastDegradationTime;
    private LocalDateTime lastRecoveryTime;

    public synchronized void recordDegradation(){

        totalDegradations++;
        lastDegradationTime = LocalDateTime.now();

        System.out.println("Degradation recorded. Total degradations: " + totalDegradations);
    }

    public synchronized void recordRecovery(){

        totalRecoveries++;
        lastRecoveryTime = LocalDateTime.now();

        System.out.println("Recovery recorded. Total recoveries: " + totalRecoveries);
    }

    public int getTotalDegradations() {
        return totalDegradations;
    }

    public int getTotalRecoveries() {
        return totalRecoveries;
    }

    public LocalDateTime getLastDegradationTime() {
        return lastDegradationTime;
    }

    public LocalDateTime getLastRecoveryTime() {
        return lastRecoveryTime;
    }
}