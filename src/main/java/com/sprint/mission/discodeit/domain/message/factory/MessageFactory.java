package com.sprint.mission.discodeit.domain.message.factory;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class MessageFactory {

    private final UserService userService;
    private final ChannelService channelService;

    public Message create(UUID speakerId, UUID channelId, String content,
                          List<BinaryContent> attachments) {

        User speaker = userService.findById(speakerId);
        Channel channel = channelService.findById(channelId);
        return Message.create(
                channel,
                speaker,
                content,
                attachments
        );
    }
}