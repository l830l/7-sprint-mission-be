package com.sprint.mission.discodeit.domain.message.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.facade.MessageCreationFacade;
import com.sprint.mission.discodeit.domain.message.factory.MessageFactory;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.binarycontent.fixture.BinaryContentFixture;
import com.sprint.mission.discodeit.domain.channel.fixture.ChannelFixture;
import com.sprint.mission.discodeit.domain.message.fixture.MessageFixture;
import com.sprint.mission.discodeit.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageCreationFacadeTest {
    @Mock
    private MessageService messageService;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private ChannelService channelService;

    @Mock
    private MessageFactory messageFactory;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private MessageCreationFacade messageCreationFacade;

    @Nested
    @DisplayName("첨부 파일 없는 메세지 생성")
    class CreateMessageWithoutAttachment {
        @Test
        @DisplayName("성공: 첨부 파일 없는 정상적인 텍스트가 들어올 경우 메세지가 생성된다")
        void success_message_create_without_attachment() {
            // given
            User user = UserFixture.createWithoutProfile();
            Channel channel = ChannelFixture.createPublicChannel();
            MessageCreateReq req = new MessageCreateReq("새 메세지", List.of());
            Message message = MessageFixture.create(channel, user, req.content(), List.of());

            given(channelService.findById(channel.getId())).willReturn(channel);
            given(messageFactory.create(user.getId(), channel.getId(), req.content(), List.of()))
                    .willReturn(message);
            given(messageService.create(message)).willReturn(message);

            // when
            MessageViewRes result = messageCreationFacade.createMessage(
                    user.getId(), channel.getId(), req);

            // then
            then(channelService).should(times(1)).findById(channel.getId());
            then(messageService).should(times(1)).create(message);
            then(binaryContentService).should(never()).upload(any());
            then(binaryContentStorage).should(never()).put(any(), any());

            assertThat(result).isNotNull();
            assertThat(result.content()).isEqualTo(req.content());
            assertThat(result.attachmentDatas()).isEmpty();
        }
    }

    @Nested
    @DisplayName("첨부파일이 존재하는 메세지 생성")
    class CreateMessageWithAttachment {
        @Test
        @DisplayName("성공: 첨부파일들과 메세지들의 파라미터가 들어올 경우 메세지가 생성된다")
        void success_message_create_with_attachment() {
            // given
            User user = UserFixture.createWithoutProfile();
            Channel channel = ChannelFixture.createPublicChannel();
            BinaryContentCreateReq imageReq = new BinaryContentCreateReq(
                    "test_image".getBytes(), "test_image.jpg", "image/jpg", 10L);
            BinaryContent binaryContent = BinaryContentFixture.create(imageReq);
            MessageCreateReq req = new MessageCreateReq("새 메세지", List.of(imageReq));
            Message message = MessageFixture.create(channel, user, req.content(), List.of(binaryContent));

            given(channelService.findById(channel.getId())).willReturn(channel);
            given(messageFactory.create(user.getId(), channel.getId(), req.content(), List.of(binaryContent)))
                    .willReturn(message);
            given(messageService.create(message)).willReturn(message);
            given(binaryContentService.upload(imageReq)).willReturn(binaryContent);

            // when
            MessageViewRes result = messageCreationFacade.createMessage(
                    user.getId(), channel.getId(), req);

            // then
            then(channelService).should(times(1)).findById(channel.getId());
            then(messageService).should(times(1)).create(message);
            then(binaryContentService).should(times(req.attachmentIds().size())).upload(any());
            then(binaryContentStorage).should(times(req.attachmentIds().size())).put(any(), any());

            assertThat(result).isNotNull();
            assertThat(result.content()).isEqualTo(req.content());
            assertThat(result.attachmentDatas().size()).isEqualTo(req.attachmentIds().size());
        }
    }
}
