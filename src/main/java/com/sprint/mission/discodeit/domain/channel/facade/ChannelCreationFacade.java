package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.channelmember.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChannelCreationFacade {

    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;
    private final ChannelMemberFactory channelMemberFactory;

    //공개 채널 추가
    @Transactional
    @PreAuthorize("#managerId == authentication.principal.userInfo.userId")
    public ChannelInfoRes createPublicChannel(UUID managerId, ChannelCreateReq req) {
        Channel channel = channelService.create(req);
        channelMemberService.create(
                channelMemberFactory.create(managerId, channel.getId(), ChannelMemberRole.OWNER));
        return ChannelMapper.toPublicResDto(channelService.get(channel.getId()));
    }

    //비밀 채널 추가
    @Transactional
    @PreAuthorize("#managerId == authentication.principal.userInfo.userId")
    public ChannelInfoRes createPrivateChannel(UUID managerId, ChannelCreateSecReq req) {
        Channel channel = channelService.create(req);
        channelMemberService.create(channelMemberFactory.create(
                managerId, channel.getId(), ChannelMemberRole.OWNER));
        req.userIds().forEach(userId -> channelMemberService.create(
                channelMemberFactory.create(userId, channel.getId(), ChannelMemberRole.MEMBER)));
        return ChannelMapper.toPrivateResDto(
                channelService.get(channel.getId()),
                req.userIds()
        );
    }
}
