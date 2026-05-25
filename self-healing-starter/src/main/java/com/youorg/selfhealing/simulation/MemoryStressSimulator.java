package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.metrics.MetricsManager;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.youorg.selfhealing.recovery.RecoveryManager;
import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class MemoryStressSimulator {

    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;
    private final RecoveryManager recoveryManager;

    public MemoryStressSimulator(
            HealthStateManager healthStateManager,
            MetricsManager metricsManager,
            RecoveryManager recoveryManager
    ) {
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
        this.recoveryManager = recoveryManager;
    }

    @GetMapping("/simulate/memory")
public String stressMemory() {

    healthStateManager.setCurrentState(
            HealthState.DEGRADED
    );
    metricsManager.recordDegradation();
    new Thread(() -> {

        try {

            Thread.sleep(15000);

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

    List<byte[]> memoryConsumer = new ArrayList<>();

    try {

        for (int i = 0; i < 20; i++) {

            memoryConsumer.add(
                    new byte[5 * 1024 * 1024]
            );

            Thread.sleep(100);
        }

    } catch (Exception e) {

        return "Memory stress interrupted";

    }

    return "Memory stress simulation started";
}
   

      
}