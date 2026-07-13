package com.refactorcheck.core.model;

public enum ImpactMode {
    ON,
    OFF;

    public static ImpactMode fromCli(String value) {
        if (value == null) {
            throw new IllegalArgumentException("--impact is required");
        }
        return switch (value.trim().toLowerCase()) {
            case "on" -> ON;
            case "off" -> OFF;
            default -> throw new IllegalArgumentException("--impact must be on or off");
        };
    }
}
