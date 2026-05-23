package com.youorg.selfhealing.health;

public class HealthStateManager {

    private HealthState currentState = HealthState.UP;

    public HealthState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(HealthState currentState) {
        this.currentState = currentState;
    }

}