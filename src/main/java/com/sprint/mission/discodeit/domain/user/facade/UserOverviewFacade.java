package com.sprint.mission.discodeit.domain.user.facade;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.user.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOverviewFacade {

    private final UserService userService;
    private final UserSessionService userSessionService;

    //유저 전체 조회
    @Transactional(readOnly = true)
    public List<UserSimpleInfoRes> findAll() {
        return userService.findAll().stream()
                .map(this::mapToSimpleInfo).toList();
    }

    //변환 메소드
    private UserSimpleInfoRes mapToSimpleInfo(User user) {
        BinaryContentInfoRes profileImg;
        profileImg = BinaryContentMapper.toResDto(user.getProfile());
        return UserMapper.toSimpleResDto(user, profileImg, userSessionService.isOnline(user.getId()));
    }
}
