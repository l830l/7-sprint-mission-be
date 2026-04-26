package com.sprint.mission.discodeit.domain.message.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    public static MessageViewRes toResDto(Message message) {
        List<BinaryContentInfoRes> attachmentDatas = message.getAttachments().stream()
                .map(BinaryContentMapper::toResDto).toList();

        return new MessageViewRes(
                message.getId(),
                message.getSpeaker().getId(),
                message.getContent(),
                attachmentDatas,
                DateTimeUtil.format(message.getCreatedAt()),
                !message.getCreatedAt().equals(message.getUpdatedAt())
        );
    }
}
