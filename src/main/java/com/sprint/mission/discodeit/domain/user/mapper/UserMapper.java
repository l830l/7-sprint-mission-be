package com.sprint.mission.discodeit.domain.user.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserSimpleInfoRes toSimpleResDto(
            User user,
            BinaryContentInfoRes profileImg,
            boolean isOnline) {
        return new UserSimpleInfoRes(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                profileImg,
                isOnline
        );
    }

    public static UserDetailInfoRes toDetailResDto(
            User user,
            BinaryContentInfoRes profileImg,
            boolean isOnline) {
        return new UserDetailInfoRes(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                profileImg,
                isOnline,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
