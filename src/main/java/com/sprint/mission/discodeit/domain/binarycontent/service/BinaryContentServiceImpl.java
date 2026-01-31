package com.sprint.mission.discodeit.domain.binarycontent.service;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(
                () -> new BinaryContentNotFoundException(ErrorCode.BINARYCONTENT_NOT_FOUNT));
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new BinaryContentNotFoundException(ErrorCode.BINARYCONTENT_NOT_FOUNT);
        }
        binaryContentRepository.deleteById(id);
    }

    // ===== 🎯 Controller Direct (DTO 반환) ======
    @Override
    @Transactional(readOnly = true)
    public BinaryContentInfoRes getBinaryContent(UUID id) {
        return BinaryContentMapper.toResDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentInfoRes> getBinaryContentList(List<UUID> binaryContentIdList) {
        return findAll().stream()
                .filter(bc -> binaryContentIdList.contains(bc.getId()))
                .map(BinaryContentMapper::toResDto)
                .toList();
    }
}