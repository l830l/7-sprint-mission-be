package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChannelDeleteFacade {

    private final ChannelService channelService;
    private final MessageService messageService;
    private final ChannelMemberService channelMemberService;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    public void deleteChannel(@NonNull UUID channelId) {
        channelMemberService.findAllByChannelId(channelId)
                .forEach(readStatus -> channelMemberService.delete(readStatus.getId()));
        messageService.findAllByChannelId(channelId).forEach(message -> {
            message.getAttachments().forEach(attachment ->
                    binaryContentStorage.delete(attachment.getId()));
            messageService.delete(message.getId());
        });
        channelService.delete(channelId);
    }
}
