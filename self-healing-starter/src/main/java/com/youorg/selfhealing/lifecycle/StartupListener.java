package com.youorg.selfhealing.lifecycle;

import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;

public class StartupListener {

    private final HealthHistoryManager historyManager;

    public StartupListener(HealthHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @PostConstruct
    public void onStartup() {
        System.out.println("==================================");
        System.out.println("SELF HEALING PLATFORM INITIALIZED");
        System.out.println("==================================");

        historyManager.addEvent(new HealthEvent(
                LocalDateTime.now(),
                "BOOT",
                "UP",
                "Platform startup completed and self-healing services initialized"
        ));
    }
}