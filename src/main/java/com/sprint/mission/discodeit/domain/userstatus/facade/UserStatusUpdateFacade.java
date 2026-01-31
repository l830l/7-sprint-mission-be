package com.sprint.mission.discodeit.domain.userstatus.facade;

import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.domain.userstatus.mapper.UserStateMapper;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusUpdateFacade {

    private final UserService userService;
    private final UserStatusService userStatusService;

    //update
    public UserStatusSimpleViewRes update(@NonNull UUID userId) {
        userService.findById(userId);     //유저 있나 확인
        userStatusService.updateByUserId(userId);
        return UserStateMapper.toSimpleResDto(userStatusService.findByUserId(userId));
    }
}
