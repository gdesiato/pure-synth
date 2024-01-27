package com.desiato.puresynth.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customOpenApi() {
        return GroupedOpenApi.builder()
                .group("spring")
                .pathsToMatch("/api/**")
                .build();
    }
}
