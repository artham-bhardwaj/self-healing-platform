package com.youorg.sampleapp.simultaion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrashSimulator {

    @GetMapping("/simulate/crash")
    public String crash() {

        new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }

            System.exit(1);

        }).start();

        return "Application will crash in 1 second...";
    }

    @GetMapping("/simulate/memory")
    public String memoryLeak() {
        return "Memory simulation endpoint triggered";
    }
}