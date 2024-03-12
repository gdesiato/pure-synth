package com.desiato.puresynth;

import com.desiato.puresynth.models.Role;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PureSynthApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(PureSynthApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Check if the default user already exists
		if (userRepository.findByUsername("defaultUser") == null) {
			// Create the default user
			User defaultUser = new User();
			defaultUser.setUsername("defaultUser");
			defaultUser.setPassword(passwordEncoder.encode("defaultPassword"));
			defaultUser.setEmail("defaultUser@mail.com");

			// Fetch or create the default role
			Role userRole = roleRepository.findByName("ROLE_USER");
			if (userRole == null) {
				userRole = new Role();
				userRole.setName("ROLE_USER");
				roleRepository.save(userRole);
			}

			// Assign the role to the user
			Set<Role> roles = new HashSet<>();
			roles.add(userRole);
			defaultUser.setRoles(roles);

			// Save the user
			userRepository.save(defaultUser);
		}

		// Hardcoding a second default user
		if (userRepository.findByUsername("dUser") == null) {
			// Create the default user
			User dUser = new User();
			dUser.setUsername("dUser");
			dUser.setPassword(passwordEncoder.encode("dPassword"));
			dUser.setEmail("dUser@mail.com");

			// Fetch or create the default role
			Role userRole = roleRepository.findByName("ROLE_USER");
			if (userRole == null) {
				userRole = new Role();
				userRole.setName("ROLE_USER");
				roleRepository.save(userRole);
			}

			// Assign the role to the user
			Set<Role> roles = new HashSet<>();
			roles.add(userRole);
			dUser.setRoles(roles);

			// Save the user
			userRepository.save(dUser);
		}

		// Check if the admin user already exists
		if (userRepository.findByUsername("adminUser") == null) {
			// Create the admin user
			User adminUser = new User();
			adminUser.setUsername("adminUser");
			adminUser.setPassword(passwordEncoder.encode("adminPassword"));
			adminUser.setEmail("adminUser@mail.com");

			// Fetch or create the admin role
			Role adminRole = roleRepository.findByName("ROLE_ADMIN");
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setName("ROLE_ADMIN");
				roleRepository.save(adminRole);
			}

			// Assign the role to the user
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			adminUser.setRoles(roles);

			// Save the user
			userRepository.save(adminUser);
		}
	}
}


