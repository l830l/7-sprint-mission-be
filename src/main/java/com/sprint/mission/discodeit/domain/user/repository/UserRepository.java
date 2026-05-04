package com.sprint.mission.discodeit.domain.user.repository;

import com.sprint.mission.discodeit.domain.user.entity.User;

import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByIdNotAndEmail(UUID id, String email);

    boolean existsByIdNotAndNickname(UUID id, String nickname);

    boolean existsByUserRole(UserRole role);
}
