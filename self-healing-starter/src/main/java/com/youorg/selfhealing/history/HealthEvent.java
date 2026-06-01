package com.youorg.selfhealing.history;

import java.time.LocalDateTime;

public class HealthEvent {

    private final LocalDateTime timestamp;
    private final String fromState;
    private final String toState;
    private final String reason;

    public HealthEvent(
            LocalDateTime timestamp,
            String fromState,
            String toState,
            String reason
    ) {
        this.timestamp = timestamp;
        this.fromState = fromState;
        this.toState = toState;
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFromState() {
        return fromState;
    }

    public String getToState() {
        return toState;
    }

    public String getReason() {
        return reason;
    }
}