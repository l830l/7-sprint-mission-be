package com.sprint.mission.discodeit.domain.channelmember.facade;

import com.sprint.mission.discodeit.domain.channelmember.dto.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.channelmember.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannerMemberCreateFacade {

    private final ChannelMemberService channelMemberService;
    private final ChannelService channelService;
    private final UserService userService;
    private final ChannelMemberFactory channelMemberFactory;

    @Transactional
    public ChannelMember create(@NonNull ChannelMemberCreateReq req) {
        if (channelService.findById(req.channelId()) == null) {
            throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        if (userService.findById(req.userId()) == null) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        return channelMemberService.create(channelMemberFactory.create(req));
    }
}
