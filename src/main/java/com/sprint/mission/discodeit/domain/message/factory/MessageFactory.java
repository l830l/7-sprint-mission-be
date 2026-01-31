package com.sprint.mission.discodeit.domain.message.factory;

import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.user.service.UserService;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageFactory {

    private final UserService userService;
    private final ChannelService channelService;

    public Message create(UUID speakerId, UUID channelId, MessageCreateReq req,
                          List<BinaryContent> attachments) {

        User speaker = userService.findById(speakerId);
        Channel channel = channelService.findById(channelId);
        return Message.create(
                channel,
                speaker,
                req.content(),
                attachments
        );
    }
}