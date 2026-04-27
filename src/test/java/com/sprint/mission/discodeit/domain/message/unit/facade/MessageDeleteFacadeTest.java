package com.sprint.mission.discodeit.domain.message.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.facade.MessageDeleteFacade;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.channel.fixture.ChannelFixture;
import com.sprint.mission.discodeit.domain.message.fixture.MessageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class MessageDeleteFacadeTest {
    @Mock
    private MessageService messageService;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private MessageDeleteFacade messageDeleteFacade;

    @Nested
    @DisplayName("메세지 삭제")
    class DeleteMessage {
        @Test
        @DisplayName("성공: 유효한 messageId 가 들어오면 message가 삭제된다")
        void success_delete_message() {
            // given
            Channel channel = ChannelFixture.createPublicChannel();
            Message message = MessageFixture.create(channel.getId());

            given(messageService.findById(message.getId())).willReturn(message);

            // when
            messageDeleteFacade.deleteMessage(message.getId());

            // then
            then(messageService).should(times(1)).findById(message.getId());
            then(binaryContentStorage).should(times(message.getAttachments().size())).delete(any(UUID.class));
            then(messageService).should(times(1)).delete(message.getId());

        }
    }
}
