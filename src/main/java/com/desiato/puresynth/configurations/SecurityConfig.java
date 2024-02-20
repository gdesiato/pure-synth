package com.desiato.puresynth.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain configure(HttpSecurity http)
            throws Exception {;

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/register").permitAll()
                        .requestMatchers("/api/users/register",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "swagger-resources",
                                "swagger-resources/**",
                                "swagger-ui/**",
                                "swagger-ui.html").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/mvc/user/**").hasRole("USER")
                        //.anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().permitAll()

        );

        http.csrf(
                c -> c.disable()
        );

        return http.build();
    }

}
