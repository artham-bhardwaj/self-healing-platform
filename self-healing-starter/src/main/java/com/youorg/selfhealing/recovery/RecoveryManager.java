package com.youorg.selfhealing.recovery;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;

public class RecoveryManager {

    private final HealthStateManager healthStateManager;

    public RecoveryManager(
            HealthStateManager healthStateManager
    ) {
        this.healthStateManager = healthStateManager;
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

                System.out.println(
                        "System recovered automatically"
                );

            } catch (Exception ignored) {
            }

        }).start();
    }
}