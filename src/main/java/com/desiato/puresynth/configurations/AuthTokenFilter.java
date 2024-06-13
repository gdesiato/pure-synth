package com.desiato.puresynth.configurations;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public AuthTokenFilter(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenValue = request.getHeader("authToken");

        if (isPublicEndpoint(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenValue == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        try {
            PureSynthToken pureSynthToken = new PureSynthToken(tokenValue);
            authenticationService.createUserDetails(pureSynthToken)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                    .ifPresent(authentication -> {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        try {
                            filterChain.doFilter(request, response);
                        } catch (IOException e) {
                            log.error("IO exception occurred while processing the filter chain", e);
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        } catch (ServletException e) {
                            log.error("Servlet exception occurred while processing the filter chain", e);
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    });
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return "/api/login".equals(requestURI) || "/api/user".equals(requestURI);
    }
}
