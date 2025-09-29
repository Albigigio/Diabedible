package com.example.diabedible.utils;

import com.example.diabedible.model.Role;
import com.example.diabedible.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides demo/sample data when enabled via configuration.
 */
public final class DemoDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataProvider.class);

    private DemoDataProvider() { }

    public static void seedUsers(UserRepository repo) {
        if (!Config.demoEnabled()) {
            LOGGER.info("DemoDataProvider disabled by config");
            return;
        }
        LOGGER.info("Seeding demo users...");
        // Common demo password
        String pwd = "123456";
        String token = HashUtils.createPasswordToken(pwd);
        // Only add if missing
        repo.findByUsername("IDMario").orElseGet(() -> {
            UserRepository.StoredUser u = new UserRepository.StoredUser("IDMario", token, Role.DIABETIC);
            repo.save(u);
            return u;
        });
        repo.findByUsername("DRGiulia").orElseGet(() -> {
            UserRepository.StoredUser u = new UserRepository.StoredUser("DRGiulia", token, Role.DOCTOR);
            repo.save(u);
            return u;
        });
        repo.findByUsername("Admin1").orElseGet(() -> {
            UserRepository.StoredUser u = new UserRepository.StoredUser("Admin1", token, Role.ADMIN);
            repo.save(u);
            return u;
        });
        LOGGER.info("Demo users seeded (password: 123456)");
    }
}
