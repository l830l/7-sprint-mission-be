package com.sprint.mission.discodeit.domain.user.dto.response;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;

import java.util.UUID;

public record UserSimpleInfoRes(
        UUID userId,                //user UUID
        String nickname,            //닉네임
        String email,               //이메일
        BinaryContentInfoRes profileImg,        //프로필 이미지
        boolean isOnline            //온라인 상태
) {

}
