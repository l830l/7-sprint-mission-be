package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelUpdateFacade {

    private final ChannelService channelService;

    @Transactional
    @PreAuthorize("""
                    hasRole('ADMIN') or
                    @channelSecurity.isPublicChannel(#id) and
                    @channelSecurity.isChannelOwner(
                        #id,
                        authentication.principal.userInfo.userId
                    )
            """)
    public ChannelInfoRes update(UUID id, ChannelUpdateReq req) {
        channelService.update(id, req);
        return ChannelMapper.toPublicResDto(channelService.get(id));
    }
}