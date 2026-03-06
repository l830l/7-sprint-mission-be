package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelCreateReq req);

    Channel create(ChannelCreateSecReq req);

    Channel update(UUID id, ChannelUpdateReq req);

    void delete(UUID id);

    Channel findById(UUID id);

    ChannelInfoQuery get(UUID id);

    List<ChannelInfoQuery> getAllByUser(UUID userId, String searchTxt);
}
