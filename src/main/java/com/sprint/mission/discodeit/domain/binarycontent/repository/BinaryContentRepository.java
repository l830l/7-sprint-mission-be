package com.sprint.mission.discodeit.domain.binarycontent.repository;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

}
