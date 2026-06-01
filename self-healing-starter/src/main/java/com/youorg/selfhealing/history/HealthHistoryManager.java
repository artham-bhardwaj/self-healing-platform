package com.youorg.selfhealing.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HealthHistoryManager {

    private final List<HealthEvent> events =
            new ArrayList<>();

    public void addEvent(
            HealthEvent event
    ) {
        events.add(event);
    }

    public List<HealthEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}