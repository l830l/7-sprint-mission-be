package com.sprint.mission.discodeit.domain.channel.facade;

import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelDeleteFacade {

    private final ChannelService channelService;
    private final MessageService messageService;
    private final ChannelMemberService channelMemberService;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @PreAuthorize("""
                    hasRole('ADMIN') or
                    @channelSecurity.isChannelOwner(
                        #channelId,
                        authentication.principal.userInfo.userId
                    )
            """)
    public void deleteChannel(UUID channelId) {
        Channel channel = channelService.findById(channelId);
        channelMemberService.findAllByChannelId(channelId)
                .forEach(channelMember -> channelMemberService.delete(channelMember.getId()));
        messageService.findAllByChannelId(channelId).forEach(message -> {
            message.getAttachments().forEach(attachment -> {
                binaryContentStorage.delete(attachment.getId());
                binaryContentService.delete(attachment.getId());
            });
            messageService.delete(message.getId());
        });
        channelService.delete(channelId);
    }
}