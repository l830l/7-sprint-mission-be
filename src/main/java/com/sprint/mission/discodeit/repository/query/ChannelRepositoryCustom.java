package com.sprint.mission.discodeit.repository.query;

import com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery;
import java.util.List;
import java.util.UUID;

public interface ChannelRepositoryCustom {

  List<ChannelInfoQuery> findAllMyChannels(UUID userId, String searchTxt);

  ChannelInfoQuery findByChannelId(UUID channelId);
}
