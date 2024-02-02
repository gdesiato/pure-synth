package com.desiato.puresynth.configurations;

import com.desiato.puresynth.models.SecurityUser;
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
            throws Exception {

//        http.formLogin(form -> form
//                .loginPage("/login")
//                .failureUrl("/login?error")
//                .successHandler((request, response, authentication) -> {
//                    SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
//                    logger.info("Login successful for user: {}, with ID: {}", userDetails.getUsername(), userDetails.getId());
//                    response.sendRedirect("/user/" + userDetails.getId());
//                })
//                .permitAll()
//        );

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/register").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/users/register",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "swagger-resources",
                                "swagger-resources/**",
                                "swagger-ui/**",
                                "swagger-ui.html").permitAll()
                        //.anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().permitAll()

        );

        http.csrf(
                c -> c.disable()
        );

        return http.build();
    }

}
