package com.sprint.mission.discodeit.domain.user.facade;


import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.binarycontent.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.domain.user.factory.UserFactory;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCreationFacade {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final UserStatusService userStatusService;
    private final UserFactory userFactory;

    //유저 추가
    @Transactional
    public UserDetailInfoRes createUser(@NonNull UserCreateReq req) {
        UUID profileId = null;

        if (req.profileImage().data() != null) {

            BinaryContent profile = binaryContentService.create(
                    BinaryContentFactory.create(req.profileImage())
            );
            profileId = profile.getId();
            binaryContentStorage.put(profileId, req.profileImage().data());
        }
        User user = userService.create(userFactory.create(req, profileId));
        userStatusService.create(UserStatus.create(user));
        return UserMapper.toDetailResDto(
                user,
                user.getProfile() == null ?
                        null : BinaryContentMapper.toResDto(user.getProfile()),
                true
        );
    }
}
