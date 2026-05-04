package com.sprint.mission.discodeit.domain.channelmember.repository;

import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, UUID> {

    boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);

    boolean existsByChannelIdAndUserIdAndRole(UUID channelId, UUID userId, ChannelMemberRole role);

    List<ChannelMember> findAllByChannelId(UUID channelId);

    List<ChannelMember> findByChannelIdAndRole(UUID channelId, ChannelMemberRole role);
}
