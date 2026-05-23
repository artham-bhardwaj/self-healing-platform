package com.youorg.selfhealing.autoconfig;

import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.health.SelfHealingHealthIndicator;
import com.youorg.selfhealing.lifecycle.StartupListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.youorg.selfhealing")
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
    public SelfHealingHealthIndicator selfHealingHealthIndicator(
            HealthStateManager healthStateManager
    ) {
        return new SelfHealingHealthIndicator(healthStateManager);
    }

}