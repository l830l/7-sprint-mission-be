package com.sprint.mission.discodeit.domain.user.dto.request;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;

public record UserUpdateReq(
        String email,
        String nickname,
        String password,
        BinaryContentCreateReq profileImage
) {

    public static UserUpdateReq from(UserInfoReq infoReq, BinaryContentCreateReq profileImage) {
        return new UserUpdateReq(infoReq.email(), infoReq.nickname(), infoReq.password(), profileImage);
    }
}
