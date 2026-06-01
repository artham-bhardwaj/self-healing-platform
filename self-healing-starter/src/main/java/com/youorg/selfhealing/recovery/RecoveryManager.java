package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import io.micrometer.core.instrument.Metrics;
import java.time.LocalDateTime;
public class RecoveryManager {

    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;
    private final HealthHistoryManager historyManager;
    public RecoveryManager(
            HealthStateManager healthStateManager,
            MetricsManager metricsManager,
            HealthHistoryManager historyManager
    ) {
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
        this.historyManager = historyManager;
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
                historyManager.addEvent(
                        new HealthEvent(
                                LocalDateTime.now(),
                                "DEGRADED",
                                "HEALTHY",
                                "automatic recovery"
                        )
                );

                System.out.println(
                        "System recovered automatically"
                );

            } catch (Exception ignored) {
            }

        }).start();
    }
}