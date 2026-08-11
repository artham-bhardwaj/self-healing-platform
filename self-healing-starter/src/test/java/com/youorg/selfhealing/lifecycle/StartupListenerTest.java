package com.youorg.selfhealing.lifecycle;

import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StartupListenerTest {

    @Test
    void onStartupRecordsInitializationEvent() {
        HealthHistoryManager historyManager = new HealthHistoryManager();
        StartupListener startupListener = new StartupListener(historyManager);

        startupListener.onStartup();

        assertFalse(historyManager.getEvents().isEmpty());
        HealthEvent event = historyManager.getEvents().getFirst();
        assertNotNull(event.getTimestamp());
        assertFalse(event.getReason().isBlank());
    }
}
