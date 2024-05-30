package com.desiato.puresynth.configurations;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.exceptions.InvalidTokenException;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private AuthenticationService authenticationService;

    public AuthTokenFilter(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    private final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

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
            Optional<CustomUserDetails> userDetailsOptional = authenticationService.authenticateByToken(pureSynthToken);

            if (userDetailsOptional.isPresent()) {
                UserDetails userDetails = userDetailsOptional.get();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (AuthenticationException | InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return "/api/login".equals(requestURI) || "/api/user".equals(requestURI);
    }
}
