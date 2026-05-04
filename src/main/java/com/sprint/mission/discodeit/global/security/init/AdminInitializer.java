package com.sprint.mission.discodeit.global.security.init;

import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.entity.UserRole;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.global.properties.AdminInitializerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminInitializerProperties adminInitializerProperties;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.existsByUserRole(UserRole.ADMIN)) return;

        User admin = User.createAdmin(
                adminInitializerProperties.email(),
                adminInitializerProperties.nickname(),
                passwordEncoder.encode(adminInitializerProperties.password())
        );

        userRepository.save(admin);
    }
}
