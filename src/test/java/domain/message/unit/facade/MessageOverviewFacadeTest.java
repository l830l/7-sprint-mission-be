package domain.message.unit.facade;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCursorReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.facade.MessageOverviewFacade;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.global.dto.response.PageResponse;
import domain.channel.fixture.ChannelFixture;
import domain.message.fixture.MessageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MessageOverviewFacadeTest {
    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageOverviewFacade messageOverviewFacade;

    @Nested
    @DisplayName("메세지 목록 조회")
    class FindAllMessages {
        @Test
        @DisplayName("성공: 정상적인 channelId 등이 들어올 경우 정상적으로 메세지 조회가 된다.")
        void success_message_find() {
            // given
            Channel channel = ChannelFixture.createPublicChannel();
            MessageCursorReq req = new MessageCursorReq(
                    channel.getId(), null, null, 10, null);
            MessageCursorQuery query = new MessageCursorQuery(
                    channel.getId(), null, null, 10, null);
            Slice<Message> slice = new SliceImpl<>(
                    List.of(MessageFixture.create(channel.getId()), MessageFixture.create(channel.getId())),
                    PageRequest.of(0, 10),
                    false
            );
            given(messageService.getMessages(query)).willReturn(slice);

            // when
            PageResponse<MessageViewRes> result = messageOverviewFacade.findAllByChannelId(req);

            // then
            then(messageService).should(times(1)).getMessages(query);
            assertThat(result).isNotNull();
        }
    }
}
