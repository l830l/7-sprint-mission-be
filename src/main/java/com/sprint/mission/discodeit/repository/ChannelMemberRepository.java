package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, UUID> {

  boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);

  List<ChannelMember> findAllByChannelId(UUID channelId);

  List<ChannelMember> findByChannelIdAndRole(UUID channelId, ChannelMemberRole role);
}
