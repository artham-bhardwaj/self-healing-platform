package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecoveryManager {

    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;
    private final HealthHistoryManager historyManager;
    private final AtomicBoolean recoveryInProgress = new AtomicBoolean(false);
    private volatile long lastRecoveryRequestTimestamp = 0L;

    public RecoveryManager(
            HealthStateManager healthStateManager,
            MetricsManager metricsManager,
            HealthHistoryManager historyManager
    ) {
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
        this.historyManager = historyManager;
    }

    public boolean isRecoveryInProgress() {
        return recoveryInProgress.get();
    }

    public long getLastRecoveryRequestTimestamp() {
        return lastRecoveryRequestTimestamp;
    }

    public void recoverAfterDelay(long delayMillis) {
        if (recoveryInProgress.getAndSet(true)) {
            return;
        }

        lastRecoveryRequestTimestamp = System.currentTimeMillis();
        new Thread(() -> {
            try {
                System.out.println("Recovery scheduled...");
                Thread.sleep(delayMillis);
                healthStateManager.setCurrentState(HealthState.UP, "recovery completed after degraded detection");
                metricsManager.recordRecovery();
                historyManager.addEvent(new HealthEvent(
                        LocalDateTime.now(),
                        "DEGRADED",
                        "HEALTHY",
                        "automatic recovery"
                ));
                System.out.println("System recovered automatically");
            } catch (Exception ignored) {
                metricsManager.recordRecoveryFailure("automatic-recovery");
            } finally {
                recoveryInProgress.set(false);
            }
        }).start();
    }
}