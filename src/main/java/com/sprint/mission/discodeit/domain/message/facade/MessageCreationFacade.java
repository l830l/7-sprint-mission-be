package com.sprint.mission.discodeit.domain.message.facade;

import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.domain.message.factory.MessageFactory;
import com.sprint.mission.discodeit.domain.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageCreationFacade {

    private final MessageService messageService;
    private final BinaryContentService binaryContentService;
    private final ChannelService channelService;
    private final MessageFactory messageFactory;
    private final BinaryContentStorage binaryContentStorage;

    //메세지 추가
    @Transactional
    public MessageViewRes createMessage(@NonNull UUID speakerId, @NonNull UUID channelId,
                                        @NonNull MessageCreateReq req) {
        if (channelService.findById(channelId) == null) {
            throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        List<BinaryContent> attachments = new ArrayList<>();
        if (!req.attachmentIds().isEmpty()) {
            req.attachmentIds().forEach(BinaryContentReq -> {
                        BinaryContent binaryContent = binaryContentService.upload(BinaryContentReq);
                        attachments.add(binaryContent);
                        binaryContentStorage.put(binaryContent.getId(), BinaryContentReq.data());
                    }
            );
        }

        Message message = messageService.create(
                messageFactory.create(speakerId, channelId, req, attachments));
        return MessageMapper.toResDto(message);
    }
}

