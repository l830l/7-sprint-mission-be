package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public BinaryContent upload(BinaryContentCreateReq request) {
        BinaryContent binaryContent = new BinaryContent(request.name(), request.type(), request.size());
        BinaryContent infoSaved = binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(infoSaved.getId(), request.data());
        return infoSaved;
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(UUID binaryContentId) {
        find(binaryContentId);
        return binaryContentStorage.download(binaryContentId);
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContent getInfo(UUID binaryContentId) {
        return find(binaryContentId);
    }

    @Override
    public void delete(UUID binaryContentId) {
        BinaryContent binaryContent = find(binaryContentId);
        binaryContentStorage.delete(binaryContentId);
        binaryContentRepository.delete(binaryContent);
    }

    private BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(
                () -> new BinaryContentNotFoundException(ErrorCode.BINARYCONTENT_NOT_FOUNT)
        );
    }
}