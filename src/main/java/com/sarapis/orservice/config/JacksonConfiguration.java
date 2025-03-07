package com.sarapis.orservice.config;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

  /**
   * Configures and creates a custom ObjectMapper
   * bean for JSON serialization and deserialization.
   * This method sets up an ObjectMapper with the following configurations:
   * - Treats null values for String and Collection types as empty.
   * - Registers JavaTimeModule for proper handling of Java 8 date/time types.
   * - Disables writing dates as timestamps,
   * using ISO-8601 compliant date/time format instead.
   *
   * @return A configured ObjectMapper instance.
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configOverride(String.class)
        .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    mapper.configOverride(Collection.class)
        .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper;
  }
}
