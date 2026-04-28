package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageDeleteFacade {

    private final MessageService messageService;
    private final BinaryContentStorage binaryContentStorage;

    //메세지 삭제
    @Transactional
    public void deleteMessage(UUID messageId) {
        Message message = messageService.findById(messageId);
        message.getAttachments().forEach(
                attachment -> binaryContentStorage.delete(attachment.getId()));
        messageService.delete(messageId);
    }
}
