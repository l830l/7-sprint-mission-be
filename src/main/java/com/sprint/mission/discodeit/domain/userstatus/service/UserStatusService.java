package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusViewRes;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {

    UserStatus create(UserStatus userStatus);

    UserStatus findByUserId(UUID userId);

    UserStatusViewRes findById(UUID id);

    void updateOfflineAt(UUID id);

    void update(UUID id);

    void updateByUserId(UUID userId);

    void delete(UUID id);
}
