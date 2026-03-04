package com.sprint.mission.discodeit.domain.user.facade;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailViewFacade {

    private final UserService userService;
    private final UserStatusService userStatusService;

    //유저 단일 조회
    @Transactional(readOnly = true)
    public UserDetailInfoRes findById(UUID userId) {
        User user = userService.findById(userId);
        return toDetailInfo(user);
    }

    //유저 단일 조회 : 닉네임
    @Transactional(readOnly = true)
    public UserDetailInfoRes findByNickname(String nickname) {
        User user = userService.findByNickname(nickname);
        return toDetailInfo(user);
    }

    //유저 단일 조회 : 이메일
    @Transactional(readOnly = true)
    public UserDetailInfoRes findByEmail(String email) {
        User user = userService.findByEmail(email);
        return toDetailInfo(user);
    }

    //변환 메소드
    private UserDetailInfoRes toDetailInfo(User user) {
        BinaryContentInfoRes profileImg;
        profileImg = BinaryContentMapper.toResDto(user.getProfile());
        UserStatus userStatus = userStatusService.findByUserId(user.getId());
        return UserMapper.toDetailResDto(user, profileImg, userStatus.isOnline());
    }
}
