package com.sprint.mission.discodeit.service.query;

import com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.repository.query.ChannelRepositoryCustom;
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
