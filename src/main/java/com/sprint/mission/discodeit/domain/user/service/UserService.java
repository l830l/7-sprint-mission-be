package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.domain.auth.dto.response.AvailabilityRes;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(User user);

    List<User> findAll();

    User findByEmail(String email);

    User findByNickname(String nickname);

    User findById(UUID id);

    void delete(UUID id);

    void update(UUID id, UserUpdateReq req);

    AvailabilityRes isRegisteredNickname(String nickname);

    AvailabilityRes isRegisteredEmail(String email);

    void sendEmailId(String email);

    void sendEmailTemporaryPassword(String email, String nickname);
}
