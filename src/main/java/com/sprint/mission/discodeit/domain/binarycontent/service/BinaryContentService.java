package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContent create(BinaryContent binaryContent);

    BinaryContent findById(UUID id);

    List<BinaryContent> findAll();

    void delete(UUID id);

    BinaryContentInfoRes getBinaryContent(UUID id);

    List<BinaryContentInfoRes> getBinaryContentList(List<UUID> binaryContentIdList);
}
