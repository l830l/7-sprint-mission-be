package com.sprint.mission.discodeit.domain.message.dto.request;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MessageCreateReq(
        @NotBlank
        String content,
        List<BinaryContentCreateReq> attachmentIds
) {
    public MessageCreateReq {
        if (attachmentIds == null) {
            attachmentIds = List.of();
        }
    }

    public static MessageCreateReq from(MessageInfoReq infoReq, List<BinaryContentCreateReq> attachmentIds) {
        return new MessageCreateReq(infoReq.content(), attachmentIds);
    }
}