package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.metrics.MetricsManager;

import io.micrometer.core.instrument.Metrics;

public class RecoveryManager {

    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;
    public RecoveryManager(
            HealthStateManager healthStateManager,
            MetricsManager metricsManager
    ) {
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
    }

    public void recoverAfterDelay(
            long delayMillis
    ) {

        new Thread(() -> {

            try {

                System.out.println(
                        "Recovery scheduled..."
                );

                Thread.sleep(delayMillis);

                healthStateManager.setCurrentState(
                        HealthState.UP
                );

                metricsManager.recordRecovery();

                System.out.println(
                        "System recovered automatically"
                );

            } catch (Exception ignored) {
            }

        }).start();
    }
}