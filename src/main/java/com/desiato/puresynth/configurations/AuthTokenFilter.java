package com.desiato.puresynth.configurations;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final AntPathMatcher antPathMatcher;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
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
            var authenticationDetails = authenticationService
                    .createUserDetails(pureSynthToken)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities())
                    );
            if (authenticationDetails.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationDetails.get());
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid token");
            }
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        boolean isPublic = antPathMatcher.match("/api/login", requestURI) ||
                antPathMatcher.match("/api/user", requestURI);
        logger.debug("Request URI: " + requestURI + " isPublic: " + isPublic);
        return isPublic;
    }
}
