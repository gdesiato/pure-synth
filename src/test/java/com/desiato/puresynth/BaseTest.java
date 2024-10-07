package com.desiato.puresynth;

import com.desiato.puresynth.configurations.AuthTokenFilter;
import com.desiato.puresynth.controllers.TestAuthenticationHelper;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.SessionService;
import com.desiato.puresynth.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TestAuthenticationHelper testAuthenticationHelper;

    @Autowired
    public SessionRepository sessionRepository;

    @Autowired
    public SessionService sessionService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService authService;

    @Autowired
    public AuthTokenFilter authTokenFilter;
}
