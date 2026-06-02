package com.youorg.selfhealing.autoconfig;

import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.lifecycle.StartupListener;
import com.youorg.selfhealing.recovery.RecoveryManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.history.HealthHistoryManager;
import com.youorg.selfhealing.metrics.MetricsConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import com.youorg.selfhealing.controller.HistoryController;

@Configuration
@ConditionalOnClass(HistoryController.class)
@ComponentScan(basePackages = {
        "com.youorg.selfhealing.health",
        "com.youorg.selfhealing.simulation",
        "com.youorg.selfhealing.controller",
        "com.youorg.selfhealing.metrics"
})
public class SelfHealingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StartupListener startupListener() {
        return new StartupListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public HealthStateManager healthStateManager() {
        return new HealthStateManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public RecoveryManager recoveryManager(
        HealthStateManager healthStateManager,
        MetricsManager metricsManager,
        HealthHistoryManager historyManager
) {
    return new RecoveryManager(
            healthStateManager,
            metricsManager,
            historyManager
    );
}

    @Bean
    @ConditionalOnMissingBean
    public MetricsManager metricsManager(){
        return new MetricsManager();
    }
    @Bean
    @ConditionalOnMissingBean
    public HealthHistoryManager healthHistoryManager() {
        return new HealthHistoryManager();
    }
    @Bean
    @ConditionalOnMissingBean
    public MetricsConfiguration metricsConfiguration(
        MeterRegistry meterRegistry,
        MetricsManager metricsManager,
        HealthStateManager healthStateManager
) {
    return new MetricsConfiguration(
            meterRegistry,
            metricsManager,
            healthStateManager
    );
}
@Bean
@ConditionalOnMissingBean
public HistoryController historyController(
        HealthHistoryManager historyManager
) {
    return new HistoryController(historyManager);
}
}