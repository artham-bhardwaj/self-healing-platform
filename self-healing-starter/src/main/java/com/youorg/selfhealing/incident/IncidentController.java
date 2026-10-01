package com.youorg.selfhealing.incident;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/self-healing")
public class IncidentController {

    private final IncidentManager incidentManager;

    public IncidentController(IncidentManager incidentManager) {
        this.incidentManager = incidentManager;
    }

    @GetMapping("/incidents")
    public List<Incident> getIncidents() {
        return incidentManager.getIncidentHistory();
    }

    @GetMapping("/incidents/active")
    public List<Incident> getActiveIncidents() {
        return incidentManager.getActiveIncidents();
    }

    @GetMapping("/incidents/summary")
    public Map<String, Object> getSummary() {
        List<Incident> incidents = incidentManager.getIncidentHistory();
        List<Incident> active = incidentManager.getActiveIncidents();
        long resolvedIncidents = incidents.stream()
                .filter(incident -> incident.getStatus() == IncidentStatus.RESOLVED)
                .count();
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncidents", incidents.size());
        summary.put("activeIncidents", active.size());
        summary.put("resolvedIncidents", resolvedIncidents);
        summary.put("criticalActiveIncidents", active.stream().filter(i -> "CRITICAL".equals(i.getSeverity())).count());
        return summary;
    }

    @GetMapping("/incidents/{id}")
    public Incident getIncident(@PathVariable long id) {
        return incidentManager.getIncident(id);
    }
}
