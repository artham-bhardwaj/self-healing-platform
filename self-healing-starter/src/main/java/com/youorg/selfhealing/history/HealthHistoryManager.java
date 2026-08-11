package com.youorg.selfhealing.history;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HealthHistoryManager {

    private final List<HealthEvent> events = new CopyOnWriteArrayList<>();

    public void addEvent(
            HealthEvent event
    ) {
        events.add(event);
    }

    public List<HealthEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
