package com.youorg.selfhealing.lifecycle;

import jakarta.annotation.PostConstruct;

public class StartupListener {

    @PostConstruct
    public void onStartup() {

        System.out.println("==================================");
        System.out.println("SELF HEALING PLATFORM INITIALIZED");
        System.out.println("==================================");

    }
}