package com.youorg.selfhealing.simulation;

import com.youorg.selfhealing.health.HealthState;
import com.youorg.selfhealing.health.HealthStateManager;
import com.youorg.selfhealing.history.HealthEvent;
import com.youorg.selfhealing.history.HealthHistoryManager;
import com.youorg.selfhealing.incident.Incident;
import com.youorg.selfhealing.incident.IncidentManager;
import com.youorg.selfhealing.incident.IncidentType;
import com.youorg.selfhealing.metrics.MetricsManager;
import com.youorg.selfhealing.recovery.RecoveryEngine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/self-healing")
public class FailureScenarioController {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final HealthStateManager healthStateManager;
    private final MetricsManager metricsManager;
    private final IncidentManager incidentManager;
    private final RecoveryEngine recoveryEngine;
    private final HealthHistoryManager historyManager;

    public FailureScenarioController(
            HealthStateManager healthStateManager,
            MetricsManager metricsManager,
            IncidentManager incidentManager,
            RecoveryEngine recoveryEngine,
            HealthHistoryManager historyManager
    ) {
        this.healthStateManager = healthStateManager;
        this.metricsManager = metricsManager;
        this.incidentManager = incidentManager;
        this.recoveryEngine = recoveryEngine;
        this.historyManager = historyManager;
    }

    @GetMapping("/simulate")
    public List<FailureScenario> listScenarios() {
        return List.of(
                new FailureScenario("cpu-spike", IncidentType.CPU_SPIKE, "payments-api", "CPU saturation detected", "The API worker threads are fully consumed by CPU-bound work.", HealthState.DEGRADED, 1500),
                new FailureScenario("memory-pressure", IncidentType.MEMORY_PRESSURE, "billing-service", "Memory pressure observed", "Heap usage crossed the recovery threshold and memory pressure is rising.", HealthState.DEGRADED, 2000),
                new FailureScenario("dependency-failure", IncidentType.DEPENDENCY_FAILURE, "checkout-service", "Dependency outage", "The downstream catalog service is timing out, blocking order evaluation.", HealthState.DEGRADED, 2500),
                new FailureScenario("latency-spike", IncidentType.LATENCY_SPIKE, "search-api", "Latency spike", "P95 latency rose above the operational threshold and degraded user response time.", HealthState.DEGRADED, 1800)
        );
    }

    @GetMapping("/simulate/trigger")
    public String triggerScenario(@RequestParam(defaultValue = "cpu-spike") String scenarioName) {
        FailureScenario scenario = listScenarios().stream()
                .filter(entry -> entry.getName().equals(scenarioName))
                .findFirst()
                .orElse(null);

        if (scenario == null) {
            return "Unknown scenario: " + scenarioName;
        }

        executor.submit(() -> {
            HealthState previousState = healthStateManager.getCurrentState();
            healthStateManager.setCurrentState(scenario.getDetectedState());
            metricsManager.recordDegradation();
            Incident incident = incidentManager.createIncident(
                    scenario.getComponent(),
                    scenario.getIncidentType(),
                    scenario.getSummary(),
                    scenario.getDescription(),
                    scenario.getDetectedState()
            );

            incident.addTimeline("SCENARIO_TRIGGERED :: " + scenario.getName());
            historyManager.addEvent(new HealthEvent(
                    LocalDateTime.now(),
                    previousState.name(),
                    scenario.getDetectedState().name(),
                    "Incident #" + incident.getId() + " detected: " + scenario.getSummary()
            ));
            try {
                Thread.sleep(scenario.getDelayMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            HealthState recoveryResult = recoveryEngine.attemptRecovery(incident);
            historyManager.addEvent(new HealthEvent(
                    LocalDateTime.now(),
                    scenario.getDetectedState().name(),
                    recoveryResult.name(),
                    "Incident #" + incident.getId() + " recovery "
                            + (recoveryResult == HealthState.UP ? "succeeded" : "failed")
                            + ": " + scenario.getSummary()
            ));
        });

        return "Scenario '" + scenarioName + "' started. Incident is being tracked and recovery will be attempted.";
    }
}
