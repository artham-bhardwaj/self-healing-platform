package com.youorg.selfhealing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "self-healing")
public class SelfHealingProperties {

    private boolean enabled = true;
    private long defaultRecoveryDelayMs = 1500;
    private int maxRecoveryAttempts = 3;
    private int incidentRetentionHours = 24;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDefaultRecoveryDelayMs() {
        return defaultRecoveryDelayMs;
    }

    public void setDefaultRecoveryDelayMs(long defaultRecoveryDelayMs) {
        this.defaultRecoveryDelayMs = defaultRecoveryDelayMs;
    }

    public int getMaxRecoveryAttempts() {
        return maxRecoveryAttempts;
    }

    public void setMaxRecoveryAttempts(int maxRecoveryAttempts) {
        this.maxRecoveryAttempts = maxRecoveryAttempts;
    }

    public int getIncidentRetentionHours() {
        return incidentRetentionHours;
    }

    public void setIncidentRetentionHours(int incidentRetentionHours) {
        this.incidentRetentionHours = incidentRetentionHours;
    }
}
