package com.sprint.mission.discodeit.domain.channelmember.mapper;

import com.sprint.mission.discodeit.domain.channelmember.dto.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMemberMapper {

    public static ChannelMemberInfoRes toResDto(ChannelMember channelMember) {
        return new ChannelMemberInfoRes(
                DateTimeUtil.format(channelMember.getCreatedAt()),
                DateTimeUtil.format(channelMember.getUpdatedAt()),
                channelMember.getUser().getId(),
                channelMember.getChannel().getId()
        );
    }
}
