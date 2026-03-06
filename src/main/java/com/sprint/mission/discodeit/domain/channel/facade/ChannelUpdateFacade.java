package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channel.service.QueryChannelService;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelUpdateFacade {

    private final ChannelService channelService;

    @Transactional
    public ChannelInfoRes update(UUID id, ChannelUpdateReq req) {
        channelService.update(id, req);
        return ChannelMapper.toPublicResDto(channelService.get(id));
    }
}
