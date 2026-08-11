package com.youorg.selfhealing.autoconfig;

import com.youorg.selfhealing.config.SelfHealingProperties;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.incident.IncidentManager;
import com.youorg.selfhealing.lifecycle.StartupListener;
import com.youorg.selfhealing.recovery.DefaultRecoveryStrategy;
import com.youorg.selfhealing.recovery.DependencyRecoveryStrategy;
import com.youorg.selfhealing.recovery.RecoveryEngine;
import com.youorg.selfhealing.recovery.RecoveryManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.history.HealthHistoryManager;
import com.youorg.selfhealing.metrics.MetricsConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import com.youorg.selfhealing.controller.HistoryController;

import java.util.List;

@Configuration
@EnableConfigurationProperties(SelfHealingProperties.class)
@ConditionalOnClass(HistoryController.class)
@ComponentScan(basePackages = {
        "com.youorg.selfhealing.health",
        "com.youorg.selfhealing.simulation",
        "com.youorg.selfhealing.controller",
        "com.youorg.selfhealing.metrics",
        "com.youorg.selfhealing.incident",
        "com.youorg.selfhealing.recovery",
        "com.youorg.selfhealing.history"
})
public class SelfHealingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StartupListener startupListener(HealthHistoryManager historyManager) {
        return new StartupListener(historyManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public HealthStateManager healthStateManager() {
        return new HealthStateManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public SelfHealingProperties selfHealingProperties() {
        return new SelfHealingProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public IncidentManager incidentManager() {
        return new IncidentManager();
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
    public DefaultRecoveryStrategy defaultRecoveryStrategy(HealthStateManager healthStateManager) {
        return new DefaultRecoveryStrategy(healthStateManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public DependencyRecoveryStrategy dependencyRecoveryStrategy() {
        return new DependencyRecoveryStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public RecoveryEngine recoveryEngine(
            IncidentManager incidentManager,
            MetricsManager metricsManager,
            DefaultRecoveryStrategy defaultRecoveryStrategy,
            DependencyRecoveryStrategy dependencyRecoveryStrategy
    ) {
        return new RecoveryEngine(
                incidentManager,
                metricsManager,
                List.of(defaultRecoveryStrategy, dependencyRecoveryStrategy)
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