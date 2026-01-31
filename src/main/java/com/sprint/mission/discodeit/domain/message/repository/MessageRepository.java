package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.entity.Message;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByChannelId(UUID channelId);

    // 특정 채널 메시지 최신순 50개 가져오기
    Slice<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
}
