package com.sarapis.orservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final List<String> ALLOWED_ORIGINS_DEV = List.of("http://localhost:5173/");
    private static List<String> ALLOWED_ORIGINS_PROD;

    // Refer to https://www.baeldung.com/spring-inject-static-field
    @Value("${prod.allowed_origins:#{null}}")
    private void setAllowedOriginsProd(String rawUrls) {
        ALLOWED_ORIGINS_PROD = Optional.ofNullable(rawUrls)
                .map(urls -> Arrays.stream(urls.split(","))
                        .map(String::trim)
                        .collect(Collectors.toUnmodifiableList()))
                .orElse(Collections.emptyList());
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**")
                        .permitAll()
                        .requestMatchers("/")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource(corsConfigurationSource(ALLOWED_ORIGINS_PROD)))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource(corsConfigurationSource(ALLOWED_ORIGINS_DEV)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**")
                        .permitAll())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(List<String> allowedOrigins) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
