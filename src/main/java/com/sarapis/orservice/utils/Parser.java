package com.sarapis.orservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Parser {
    public static Integer parseToInteger(String str) {
        return Integer.parseInt(str);
    }

    // TODO: Implement
    public static <T> List<T> parseToEntityList(String str, Class<T> entityType) {
        throw new RuntimeException();
    }

    public static <T> T parseToEntity(String str, Class<T> entityType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(str, entityType);
    }
}
