package org.example.real_estate_agency;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin") == null) {
            User admin = new User("admin", "admin", "ADMIN");
            userService.save(admin);
        }
    }
}