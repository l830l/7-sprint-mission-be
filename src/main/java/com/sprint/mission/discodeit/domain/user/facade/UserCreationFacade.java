package com.sprint.mission.discodeit.domain.user.facade;


import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.factory.UserFactory;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    //유저 추가
    @Transactional
    public UserDetailInfoRes createUser(UserCreateReq req) {
        User user = null;
        String encodedPassword = passwordEncoder.encode(req.password());

        if (req.profileImage() != null && req.profileImage().data() != null) {
            BinaryContent profile = binaryContentService.upload(req.profileImage());
            user = userService.create(userFactory.create(req, encodedPassword, profile.getId()));
            binaryContentStorage.put(profile.getId(), req.profileImage().data());
        } else {
            user = userService.create(userFactory.create(req, encodedPassword, null));
        }

        userStatusService.create(UserStatus.create(user));
        return UserMapper.toDetailResDto(
                user,
                user.getProfile() == null ?
                        null : BinaryContentMapper.toResDto(user.getProfile()),
                true
        );
    }
}
