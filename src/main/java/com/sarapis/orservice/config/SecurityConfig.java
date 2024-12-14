package com.sarapis.orservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  @Profile("prod")
  public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable).build();
  }

  @Bean
  @Profile("dev")
  public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authorize ->
        authorize
            .requestMatchers("/**").permitAll()
    ).build();
  }
}
