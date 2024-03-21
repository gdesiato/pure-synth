package com.desiato.puresynth.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // only runs in the development profile
public class PasswordEncodingChecker implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncodingChecker(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Use the password encoder to encode a raw password
        String rawPassword = "defaultPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        String rawPasswordAdmin = "adminPassword";
        String encodedPasswordAdmin = passwordEncoder.encode(rawPasswordAdmin);

        System.out.println("Encoded password: " + encodedPassword);

        boolean doesMatch = passwordEncoder.matches(
                "defaultPassword",
                "$2a$10$3QrBY238gFMSmJJWsYTXtutjrX4vSNGEXe42t6SMSibfGjXzn/qZy");

        System.out.println("Password matches: " + doesMatch);

        System.out.println("Encoded admin password: " + encodedPasswordAdmin);
        boolean doesMatch2 = passwordEncoder.matches(
                "adminPassword",
                "$2a$10$.NaOgXx1gYXY0BKsrh2l/O7TDPr6B/seZR3/r/tdfHUzaCLexDTCC");

        System.out.println("Password admin matches: " + doesMatch2);


    }


}
