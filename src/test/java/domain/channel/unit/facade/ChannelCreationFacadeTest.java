package domain.channel.unit.facade;

import com.sprint.mission.discodeit.domain.BaseEntity;
import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.domain.channel.dto.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.facade.ChannelCreationFacade;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.channelmember.factory.ChannelMemberFactory;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import domain.channel.fixture.ChannelFixture;
import domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelCreationFacadeTest {
    @Mock
    private ChannelService channelService;

    @Mock
    private ChannelMemberService channelMemberService;

    @Mock
    private ChannelMemberFactory channelMemberFactory;

    @InjectMocks
    private ChannelCreationFacade channelCreationFacade;

    @Nested
    @DisplayName("공개 채널 생성")
    class CreatePublicChannel {

        @Test
        @DisplayName("성공: 매니저 id와 채널 생성 요청이 들어오면 공개 채널이 생성된다")
        void success_create_public_channel() {
            // given
            User manager = UserFixture.createWithoutProfile();
            ChannelCreateReq req = new ChannelCreateReq("채널", "설명");
            Channel channel = ChannelFixture.createPublicChannel(req.name(), req.description());
            ChannelMember channelMember = ChannelMember.create(manager, channel, ChannelMemberRole.MANAGER);
            ChannelInfoQuery query = ChannelFixture.toInfoQuery(channel, manager.getId());

            given(channelService.create(req)).willReturn(channel);
            given(channelMemberFactory.create(manager.getId(), channel.getId(), ChannelMemberRole.MANAGER))
                    .willReturn(channelMember);
            given(channelMemberService.create(channelMember)).willReturn(channelMember);
            given(channelService.get(channel.getId())).willReturn(query);

            // when
            ChannelPublicInfoRes result = (ChannelPublicInfoRes) channelCreationFacade
                    .createPublicChannel(manager.getId(), req);

            // then
            assertThat(result).isNotNull();

            then(channelService).should(times(1)).create(req);
            then(channelMemberFactory).should(times(1))
                    .create(manager.getId(), channel.getId(), ChannelMemberRole.MANAGER);
            then(channelMemberService).should(times(1)).create(channelMember);
            then(channelService).should(times(1)).get(channel.getId());
        }
    }

    @Nested
    @DisplayName("비밀 채널 생성")
    class CreatePrivateChannel {
        @Test
        @DisplayName("성공: 매니저 id와 유저 목록이 들어오면 비밀 채널이 생성된다")
        void success_create_private_channel() {
            // given
            User manager = UserFixture.createWithoutProfile();
            List<User> members = UserFixture.createMixedList();
            List<UUID> memberIds = members.stream().map(BaseEntity::getId).toList();
            ChannelCreateSecReq req = new ChannelCreateSecReq(memberIds);
            Channel channel = ChannelFixture.createPrivateChannel();
            ChannelMember channelMemberManager = ChannelMember.create(manager, channel, ChannelMemberRole.MANAGER);
            ChannelInfoQuery query = ChannelFixture.toInfoQuery(channel, manager.getId());

            given(channelService.create(req)).willReturn(channel);
            given(channelMemberFactory.create(manager.getId(), channel.getId(), ChannelMemberRole.MANAGER))
                    .willReturn(channelMemberManager);
            given(channelMemberService.create(channelMemberManager)).willReturn(channelMemberManager);
            members.forEach(user -> {
                ChannelMember member = ChannelMember.create(user, channel, ChannelMemberRole.MEMBER);
                given(channelMemberFactory.create(
                        user.getId(),
                        channel.getId(),
                        ChannelMemberRole.MEMBER
                )).willReturn(member);
                given(channelMemberService.create(member)).willReturn(member);
            });
            given(channelService.get(channel.getId())).willReturn(query);

            // when
            ChannelPrivateInfoRes result = (ChannelPrivateInfoRes) channelCreationFacade.createPrivateChannel(manager.getId(), req);

            // then
            assertThat(result).isNotNull();

            then(channelService).should(times(1)).create(req);
            then(channelMemberFactory).should(times(1)).create(manager.getId(), channel.getId(), ChannelMemberRole.MANAGER);
            then(channelMemberService)
                    .should(times(1))
                    .create(argThat(cm -> cm.getRole() == ChannelMemberRole.MANAGER));
            then(channelMemberFactory).should(times(members.size())).create(any(UUID.class), eq(channel.getId()), eq(ChannelMemberRole.MEMBER));
            then(channelMemberService)
                    .should(times(members.size()))
                    .create(argThat(cm -> cm.getRole() == ChannelMemberRole.MEMBER));
            then(channelService).should(times(1)).get(channel.getId());
        }
    }
}
