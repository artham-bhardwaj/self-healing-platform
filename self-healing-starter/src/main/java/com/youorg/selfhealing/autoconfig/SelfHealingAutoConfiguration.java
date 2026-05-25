package com.youorg.selfhealing.autoconfig;

import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.lifecycle.StartupListener;
import com.youorg.selfhealing.recovery.RecoveryManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.youorg.selfhealing.metrics.MetricsManager;

@Configuration
@ComponentScan(basePackages = {
        "com.youorg.selfhealing.health",
        "com.youorg.selfhealing.simulation",
        "com.youorg.selfhealing.controller",
        "com.youorg.selfhealing.metrics"
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
        HealthStateManager healthStateManager,
        MetricsManager metricsManager
) {
    return new RecoveryManager(
            healthStateManager,
            metricsManager
    );
}

    @Bean
    public MetricsManager metricsManager(){
        return new MetricsManager();
    }
}