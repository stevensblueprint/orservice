package com.sarapis.orservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EnableWebSecurity
@Component
public class PublicEndpointScanner {

    private final RequestMappingHandlerMapping handlerMapping;
    @Getter
    private final Set<String> publicEndpoints = new HashSet<>();

    @Autowired
    public PublicEndpointScanner(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void init() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        handlerMethods.forEach((mappingInfo, handlerMethod) -> {
            boolean isPublic = handlerMethod.hasMethodAnnotation(PublicEndpoint.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(PublicEndpoint.class);

            if (!isPublic) {
                return;
            }

            PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getPathPatternsCondition();
            if (pathPatternsCondition != null) {
                Set<String> patterns = pathPatternsCondition.getPatternValues();
                publicEndpoints.addAll(patterns);
                return;
            }

            if (mappingInfo.getPatternsCondition() != null) {
                Set<String> patterns = mappingInfo.getPatternsCondition().getPatterns();
                publicEndpoints.addAll(patterns);
            }
        });
    }
}
