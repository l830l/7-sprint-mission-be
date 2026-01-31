package com.sprint.mission.discodeit.domain.userstatus.mapper;

import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusViewRes;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStateMapper {

    public static UserStatusSimpleViewRes toSimpleResDto(UserStatus userStatus) {
        return new UserStatusSimpleViewRes(userStatus.isOnline());
    }

    public static UserStatusViewRes toDetailResDto(UserStatus userStatus) {
        return new UserStatusViewRes(
                userStatus.getUser().getId(),
                userStatus.isOnline(),
                DateTimeUtil.format(userStatus.getOfflineAt())
        );
    }
}
