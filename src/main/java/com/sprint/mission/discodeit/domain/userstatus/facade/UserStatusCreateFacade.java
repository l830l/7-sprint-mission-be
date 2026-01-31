package com.sprint.mission.discodeit.domain.userstatus.facade;

import com.sprint.mission.discodeit.domain.userstatus.dto.request.UserStatusCreateReq;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusCreateFacade {

    private final UserStatusService userStatusService;
    private final UserService userService;

    //UserState
    public UserStatus create(@NonNull UserStatusCreateReq req) {
        User user = userService.findById(req.userId());
        return userStatusService.create(UserStatus.create(user));
    }
}
