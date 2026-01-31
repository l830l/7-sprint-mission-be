package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.service.QueryChannelService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class ChannelOverViewFacade {

    private final QueryChannelService queryChannelService;

    //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
    @Transactional(readOnly = true)
    public Map<ChannelType, List<ChannelInfoRes>> findAllMyChannels(UUID userId,
                                                                    String searchTxt) {
        String normalizedSearch = (searchTxt == null || searchTxt.trim().isEmpty()) ? "" : searchTxt;

        return queryChannelService.getAllByUser(userId, normalizedSearch).stream()
                .collect(Collectors.groupingBy(
                        ChannelInfoQuery::getPublicType,
                        Collectors.mapping(
                                ChannelMapper::toResDto,
                                Collectors.toList()
                        )
                ));
    }
}
