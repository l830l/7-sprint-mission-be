package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelInvalideTypeException;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ChannelOverViewFacade {

    private final ChannelMemberService channelMemberService;
    private final ChannelService channelService;

    //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
    @Transactional(readOnly = true)
    public Map<ChannelType, List<ChannelInfoRes>> findAllMyChannels(UUID userId,
                                                                    String searchTxt) {
        String normalizedSearch = (searchTxt == null || searchTxt.trim().isEmpty()) ? "" : searchTxt;

        return channelService.getAllByUser(userId, normalizedSearch).stream()
                .collect(Collectors.groupingBy(
                        ChannelInfoQuery::getPublicType,
                        Collectors.mapping(channel -> switch (channel.getPublicType()) {
                            case PUBLIC -> ChannelMapper.toPublicResDto(channel);
                            case PRIVATE -> ChannelMapper.toPrivateResDto(
                                    channel,
                                    channelMemberService
                                            .findAllByChannelId(channel.getChannelId())
                                            .stream()
                                            .map(cm -> cm.getUser().getId())
                                            .toList()
                            );
                            default -> throw new ChannelInvalideTypeException(ErrorCode.INVALID_CHANNEL_TYPE);
                        }, Collectors.toList())
                ));
    }
}
