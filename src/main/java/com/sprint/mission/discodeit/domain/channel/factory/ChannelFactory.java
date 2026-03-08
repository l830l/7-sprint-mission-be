package com.sprint.mission.discodeit.domain.channel.factory;

import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelFactory {

    public Channel create(ChannelCreateReq req) {
        return Channel.createPublic(
                req.name(),
                req.description()
        );
    }

    public Channel create(ChannelCreateSecReq req) {
        return Channel.createPrivate();
    }
}
