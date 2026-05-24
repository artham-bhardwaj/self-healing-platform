package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.recovery.RecoveryManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuStressSimulator {

    private final HealthStateManager healthStateManager;
    private final RecoveryManager recoveryManager;

    public CpuStressSimulator(
            HealthStateManager healthStateManager,
            RecoveryManager recoveryManager
    ) {
        this.healthStateManager = healthStateManager;
        this.recoveryManager = recoveryManager;
    }

    @GetMapping("/simulate/cpu")
    public String stressCpu() {

        healthStateManager.setCurrentState(
                HealthState.DEGRADED
        );

        recoveryManager.recoverAfterDelay(
                15000
        );

        Runnable cpuTask = () -> {

            long endTime =
                    System.currentTimeMillis() + 10000;

            while (
                    System.currentTimeMillis() < endTime
            ) {

                Math.sqrt(
                        Math.random() * 999999
                );

            }
        };

        for (int i = 0; i < 10; i++) {

            new Thread(cpuTask).start();

        }

        return "CPU stress simulation started";
    }
}