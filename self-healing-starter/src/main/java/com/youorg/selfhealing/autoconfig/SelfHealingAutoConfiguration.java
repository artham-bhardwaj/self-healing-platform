package com.youorg.selfhealing.autoconfig;

import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.lifecycle.StartupListener;
import com.youorg.selfhealing.recovery.RecoveryManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.youorg.selfhealing.health",
        "com.youorg.selfhealing.simulation"
})
public class SelfHealingAutoConfiguration {

    @Bean
    public StartupListener startupListener() {
        return new StartupListener();
    }

    @Bean
    public HealthStateManager healthStateManager() {
        return new HealthStateManager();
    }

    @Bean
    public RecoveryManager recoveryManager(
            HealthStateManager healthStateManager
    ) {
        return new RecoveryManager(
                healthStateManager
        );
    }
}