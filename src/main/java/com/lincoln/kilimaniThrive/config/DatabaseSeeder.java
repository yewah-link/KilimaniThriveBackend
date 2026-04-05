package com.lincoln.kilimaniThrive.config;

import com.lincoln.kilimaniThrive.models.entity.RoleEntity;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.repositories.RoleEntityRepository;
import com.lincoln.kilimaniThrive.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder {

    private final UserRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Seed Roles
            var adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("ROLE_ADMIN").build()));
            var userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("ROLE_USER").build()));

            // Admin user
            User admin = userRepository.findByEmail("admin@kilimani.com")
                    .orElse(User.builder().email("admin@kilimani.com").build());
            
            admin.setFirstName("Admin");
            admin.setLastName("Thrive");
            admin.setPassword(passwordEncoder.encode("Rocks!123"));
            admin.setActive(true);
            admin.setRoles(new java.util.HashSet<>(java.util.List.of(adminRole)));
            admin.setPhoneNumber("0700000000");
            userRepository.save(admin);
            log.info("Admin user synced with ROLE_ADMIN: admin@kilimani.com");

            // Test user
            User testUser = userRepository.findByEmail("abrahamyewah@gmail.com")
                    .orElse(User.builder().email("abrahamyewah@gmail.com").build());
            
            testUser.setFirstName("Abraham");
            testUser.setLastName("Yewah");
            testUser.setPassword(passwordEncoder.encode("Rocks!123"));
            testUser.setActive(true);
            testUser.setRoles(new java.util.HashSet<>(java.util.List.of(userRole)));
            testUser.setPhoneNumber("0712345678");
            userRepository.save(testUser);
            log.info("Test user synced with ROLE_USER: abrahamyewah@gmail.com");
        };
    }
}
