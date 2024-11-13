package com.sarapis.orservice.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request.requestMatchers("/").permitAll()
        .requestMatchers("/api/*").hasAnyRole("ADMIN", "USER").anyRequest().authenticated());
    return http.build();
  }

  @Bean
  @Profile("prod")
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      try {
        OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) new ArrayList<>(authorities).get(0);
        mappedAuthorities = ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups"))
            .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
      } catch (Exception e) {
        logger.error("Not Authorized", e);
      }
      return mappedAuthorities;
    };
  }

  @Bean
  @Profile("dev")
  public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request -> request.requestMatchers("/").permitAll()
        .anyRequest().permitAll());
    return http.build();
  }
}
