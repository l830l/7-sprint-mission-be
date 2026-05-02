package com.sprint.mission.discodeit.domain.auth.facade;

import com.sprint.mission.discodeit.domain.auth.dto.request.UserLoginReq;
import com.sprint.mission.discodeit.domain.auth.exception.InvalidLoginException;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthFacade {

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    //로그인
    @Transactional
    public UserDetailInfoRes login(@NonNull UserLoginReq req) {
        User user = userService.findByNickname(req.nickname());
        if (user == null) {
            throw new InvalidLoginException(ErrorCode.INVALID_LOGIN);
        }
        if (!user.getPassword().equals(req.password())) {
            throw new InvalidLoginException(ErrorCode.INVALID_LOGIN);
        }

        UserStatus userStatus = userStatusService.findByUserId(user.getId());
        userStatusService.updateOfflineAt(userStatus.getId());
        return UserMapper.toDetailResDto(user,
                user.getProfile() == null ? null
                        : BinaryContentMapper.toResDto(binaryContentService.getInfo(user.getProfile().getId())),
                true);
    }

    //로그아웃
    @Transactional
    @PreAuthorize("#userId == authentication.principal.userInfo.userId")
    public void logout(@NonNull UUID userId) {
        userStatusService.findByUserId(userId);
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatusService.updateOfflineAt(userStatus.getId());
    }
}
