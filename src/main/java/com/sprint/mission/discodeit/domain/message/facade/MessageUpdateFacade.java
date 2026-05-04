package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageUpdateFacade {

    private final MessageService messageService;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @PreAuthorize("@messageSecurity.canUpdate(#messageId, @loginUser.userId())")
    public MessageViewRes updateMessage(UUID messageId, MessageUpdateReq req) {
        Message message = messageService.findById(messageId);
        // 기존 첨부파일 중 삭제할 파일 처리
        message.getAttachments().stream()
                .filter(att -> !req.keepAttachmentIds().contains(att.getId()))
                .forEach(att -> {
                    binaryContentService.delete(att.getId()); //DB삭제
                    binaryContentStorage.delete(att.getId()); //파일 삭제
                });
        message.getAttachments().removeIf(att ->
                !req.keepAttachmentIds().contains(att.getId())); //컬렉션에서 삭제

        // 새로운 첨부파일 생성
        List<BinaryContent> newAttachments = req.newAttachmentReqs().stream()
                .map(r -> {
                    BinaryContent binaryContent = binaryContentService.upload(r);
                    binaryContentStorage.put(binaryContent.getId(), r.data());
                    return binaryContent;
                }).toList();

        message.getAttachments().addAll(newAttachments);

        // 메시지 내용 업데이트
        messageService.update(messageId, req.content(), message.getAttachments());
        return MessageMapper.toResDto(message);
    }
}

