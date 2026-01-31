package com.sprint.mission.discodeit.domain.channel.repository;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;

import java.util.List;
import java.util.UUID;

public interface ChannelRepositoryCustom {

    List<ChannelInfoQuery> findAllMyChannels(UUID userId, String searchTxt);

    ChannelInfoQuery findByChannelId(UUID channelId);
}
