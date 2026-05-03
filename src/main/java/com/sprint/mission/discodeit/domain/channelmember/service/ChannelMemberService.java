package com.sprint.mission.discodeit.domain.channelmember.service;

import com.sprint.mission.discodeit.domain.channelmember.dto.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;

import java.util.List;
import java.util.UUID;

public interface ChannelMemberService {

    ChannelMember create(ChannelMember channelMember);

    ChannelMember update(UUID id);

    void delete(UUID id);

    ChannelMember findManagerByChannelId(UUID channelId);

    List<ChannelMember> findMembersByChannelId(UUID channelId);

    List<ChannelMember> findAllByChannelId(UUID channelId);

    ChannelMemberInfoRes findById(UUID id);

    boolean isManager(UUID channelId, UUID userId);

    boolean isMember(UUID channelId, UUID userId);
}
