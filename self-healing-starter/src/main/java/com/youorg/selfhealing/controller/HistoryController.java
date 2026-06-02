package com.youorg.selfhealing.controller;

import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/self-healing")
public class HistoryController {

    private final HealthHistoryManager historyManager;

    public HistoryController(
            HealthHistoryManager historyManager
    ) {
        this.historyManager = historyManager;
    }

    @GetMapping("/history")
    public List<HealthEvent> getHistory() {
        return historyManager.getEvents();
    }
}