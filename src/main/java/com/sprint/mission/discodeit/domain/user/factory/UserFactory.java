package com.sprint.mission.discodeit.domain.user.factory;

import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final BinaryContentService binaryContentService;

    public User create(UserCreateReq req, String encodedPassword, UUID profileId) {
        if (profileId == null) {
            return User.createWithoutProfile(
                    req.email(),
                    req.nickname(),
                    encodedPassword
            );
        }
        BinaryContent profile = binaryContentService.getInfo(profileId);
        return User.createWithProfile(
                req.email(),
                req.nickname(),
                encodedPassword,
                profile
        );
    }
}