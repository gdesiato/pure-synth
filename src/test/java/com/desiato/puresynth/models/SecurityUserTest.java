package com.desiato.puresynth.models;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityUserTest {

    @Test
    public void testGetAuthorities() {
        // Set up
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("USER"));
        roles.add(new Role("ADMIN"));

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@example.com");
        user.setPassword("password");
        user.setRoles(roles);

        SecurityUser securityUser = new SecurityUser(user);

        // Execute
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        // Verify
        boolean hasRoleUser = false;
        boolean hasRoleAdmin = false;
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_USER".equals(authority.getAuthority())) {
                hasRoleUser = true;
            }
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                hasRoleAdmin = true;
            }
        }

        assertTrue(hasRoleUser, "Authorities should include ROLE_USER");
        assertTrue(hasRoleAdmin, "Authorities should include ROLE_ADMIN");
    }
}
