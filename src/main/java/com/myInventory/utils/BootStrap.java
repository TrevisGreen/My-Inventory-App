package com.myInventory.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootStrap implements ApplicationListener<ContextRefreshedEvent> {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    // Declarations of the userRepository class and the passwordEncoder
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to validate roles and users, if there's no user it'll create admin
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Validating Roles");
        Role adminRole = roleRepository.findOne("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            adminRole = roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findOne("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }

        log.info("Validating Users");
        User admin = userRepository.findOne("admin@kinetic.com");
        if (admin == null) {
            String password = passwordEncoder.encode("admin");
            admin = new User("admin@kinetic.com", password, "Admin", "User");
            admin.addRole(adminRole);
            admin.addRole(userRole);
            userRepository.save(admin);
        }

        log.info("Application is running!");
    }
}
