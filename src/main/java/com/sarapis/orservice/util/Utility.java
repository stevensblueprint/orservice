package com.sarapis.orservice.util;

public class Utility {
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
