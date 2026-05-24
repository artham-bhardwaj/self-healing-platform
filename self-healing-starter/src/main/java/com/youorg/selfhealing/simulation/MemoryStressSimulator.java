package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class MemoryStressSimulator {

    private final HealthStateManager healthStateManager;

    public MemoryStressSimulator(
            HealthStateManager healthStateManager
    ) {
        this.healthStateManager = healthStateManager;
    }

    @GetMapping("/simulate/memory")
public String stressMemory() {

    healthStateManager.setCurrentState(
            HealthState.DEGRADED
    );

    new Thread(() -> {

        try {

            Thread.sleep(15000);

            healthStateManager.setCurrentState(
                    HealthState.UP
            );

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