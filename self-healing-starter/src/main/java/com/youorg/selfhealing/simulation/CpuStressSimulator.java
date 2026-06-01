package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.recovery.RecoveryManager;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class CpuStressSimulator {

    private final HealthStateManager healthStateManager;
    private final RecoveryManager recoveryManager;
    private final MetricsManager metricsManager;
    private final HealthHistoryManager historyManager;
    public CpuStressSimulator(
            HealthStateManager healthStateManager,
            RecoveryManager recoveryManager,
            MetricsManager metricsManager,
            HealthHistoryManager historyManager
    ) {
        this.healthStateManager = healthStateManager;
        this.recoveryManager = recoveryManager;
        this.metricsManager = metricsManager;
        this.historyManager = historyManager;
    }

    @GetMapping("/simulate/cpu")
    public String stressCpu() {

        healthStateManager.setCurrentState(
                HealthState.DEGRADED
        );
        historyManager.addEvent(
        new HealthEvent(
                LocalDateTime.now(),
                "HEALTHY",
                "DEGRADED",
                "memory stress"
        )
);
        metricsManager.recordDegradation();
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