package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageDeleteFacade {

    private final MessageService messageService;
    private final BinaryContentStorage binaryContentStorage;

    //메세지 삭제
    @Transactional
    public void deleteMessage(@NonNull UUID messageId) {
        Message message = messageService.findById(messageId);
        message.getAttachments().forEach(
                attachment -> binaryContentStorage.delete(attachment.getId()));
        messageService.delete(messageId);
    }
}
