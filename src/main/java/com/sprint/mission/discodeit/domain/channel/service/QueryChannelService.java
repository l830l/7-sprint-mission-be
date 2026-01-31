package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepositoryCustom;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QueryChannelService {

    private final ChannelRepositoryCustom channelRepositoryCustom;

    public List<ChannelInfoQuery> getAllByUser(UUID userId, String searchTxt) {
        return channelRepositoryCustom.findAllMyChannels(userId,
                searchTxt);
    }

    public ChannelInfoQuery get(UUID channelId) {
        return channelRepositoryCustom.findByChannelId(channelId);
    }
}
