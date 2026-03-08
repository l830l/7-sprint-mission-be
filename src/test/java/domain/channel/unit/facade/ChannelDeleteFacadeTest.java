package domain.channel.unit.facade;

import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.facade.ChannelDeleteFacade;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import domain.channel.fixture.ChannelFixture;
import domain.channelmember.fixture.ChannelMemberFixture;
import domain.message.fixture.MessageFixture;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelDeleteFacadeTest {
    @Mock
    private ChannelService channelService;

    @Mock
    private MessageService messageService;

    @Mock
    private ChannelMemberService channelMemberService;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private BinaryContentService binaryContentService;

    @InjectMocks
    private ChannelDeleteFacade channelDeleteFacade;

    @Nested
    @DisplayName("채널 삭제")
    class DeleteChannel {
        @Test
        @DisplayName("성공: 유효한 channelId가 들어올 경우 채널이 삭제된다")
        void success_delete_channel() {
            // given
            Channel channel = ChannelFixture.createPublicChannel();
            given(channelMemberService.findAllByChannelId(channel.getId()))
                    .willReturn(List.of());
            given(messageService.findAllByChannelId(channel.getId()))
                    .willReturn(List.of());

            // when
            channelDeleteFacade.deleteChannel(channel.getId());

            // then
            then(channelService).should(times(1)).delete(channel.getId());
        }

        @Test
        @DisplayName("성공: 유효한 channelId가 들어올 경우 채널, 채널 안의 메세지, 첨부파일이 삭제된다")
        void success_delete_channel_with_message_and_attachment() {
            // given
            Channel channel = ChannelFixture.createPublicChannel();
            List<ChannelMember> channelMemberList = UserFixture.createMixedList().stream()
                    .map(user -> ChannelMemberFixture.create(user, channel))
                    .toList();
            List<Message> messagelist = MessageFixture.createList(channel.getId());

            given(channelMemberService.findAllByChannelId(channel.getId()))
                    .willReturn(channelMemberList);
            given(messageService.findAllByChannelId(channel.getId()))
                    .willReturn(messagelist);

            // when
            channelDeleteFacade.deleteChannel(channel.getId());

            // then
            for (ChannelMember channelMember : channelMemberList) {
                then(channelMemberService).should(times(1))
                        .delete(channelMember.getId());
            }
            for (Message message : messagelist) {
                then(messageService).should(times(1))
                        .delete(message.getId());
                message.getAttachments().forEach(attachment -> {
                    then(binaryContentStorage).should(times(1))
                            .delete(attachment.getId());
                    then(binaryContentService).should(times(1))
                            .delete(attachment.getId());
                });
            }
            then(channelService).should(times(1)).delete(channel.getId());
        }
    }
}
