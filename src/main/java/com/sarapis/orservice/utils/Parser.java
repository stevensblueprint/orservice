package com.sarapis.orservice.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Parser {
    public static <T> BiConsumer<T, String> parseIntegerAndSet(BiConsumer<T, Integer> setter) {
        return (o, s) -> setter.accept(o, Integer.parseInt(s));
    }

    public static <T, E> BiConsumer<T, String> parseObjectAndSet(BiConsumer<T, E> setter) {
        TypeReference<E> entityType = new TypeReference<>() {};
        ObjectMapper mapper = new ObjectMapper();
        return (o, s) -> setter.accept(o, mapper.convertValue(s, entityType));
    }
}
