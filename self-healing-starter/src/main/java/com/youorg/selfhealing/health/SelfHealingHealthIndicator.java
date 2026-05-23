package com.youorg.selfhealing.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class SelfHealingHealthIndicator implements HealthIndicator {

    private final HealthStateManager healthStateManager;

    public SelfHealingHealthIndicator(HealthStateManager healthStateManager) {
        this.healthStateManager = healthStateManager;
    }

    @Override
    public Health health() {

        HealthState state = healthStateManager.getCurrentState();

        return switch (state) {

            case UP -> Health.up()
                    .withDetail("self-healing-state", "UP")
                    .build();

            case DEGRADED -> Health.status("DEGRADED")
                    .withDetail("self-healing-state", "DEGRADED")
                    .build();

            case DOWN -> Health.down()
                    .withDetail("self-healing-state", "DOWN")
                    .build();
        };
    }
}