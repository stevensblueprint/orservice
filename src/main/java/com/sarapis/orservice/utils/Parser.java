package com.sarapis.orservice.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Parser {
    public static <T> BiConsumer<T, String> parseIntegerAndSet(BiConsumer<T, Integer> setter) {
        return (o, s) -> setter.accept(o, parseIntegerOrNull(s));
    }

    public static <T, E> BiConsumer<T, String> parseObjectAndSet(BiConsumer<T, E> setter) {
        TypeReference<E> entityType = new TypeReference<>() {};
        ObjectMapper mapper = new ObjectMapper();
        return (o, s) -> setter.accept(o, mapper.convertValue(s, entityType));
    }

    public static <T> BiConsumer<T, String> parseDateAndSet(BiConsumer<T, LocalDate> setter) {
        return (o, s) -> setter.accept(o, parseDateOrNull(s));
    }

    public static <T, E extends Enum<E>> BiConsumer<T, String> parseEnumAndSet(BiConsumer<T, E> setter, Class<E> enumClass) {
        return (o, s) -> setter.accept(o, Enum.valueOf(enumClass, s));
    }

    private static Integer parseIntegerOrNull(String s) {
        return s.isEmpty() ? null : Integer.parseInt(s);
    }

    private static LocalDate parseDateOrNull(String s) {
        return s.isEmpty() ? null : LocalDate.parse(s);
    }
}
