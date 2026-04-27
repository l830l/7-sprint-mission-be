package com.sprint.mission.discodeit.domain.channel.slice.repository;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.channel.util.ChannelTestUtils;
import com.sprint.mission.discodeit.domain.channelmember.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.global.config.TestJpaAuditing;
import com.sprint.mission.discodeit.global.config.TestQueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({TestQueryDslConfig.class, TestJpaAuditing.class})
public class ChannelRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Nested
    @DisplayName("채널 목록 조회")
    class FindAllMyChannels {
        @Test
        @DisplayName("""
                        성공: 키워드 없이 채널 목록을 조회하면
                        공개 채널과 내가 참여한 비공개 채널을 모두 조회한다
                """)
        void success_find_all_my_channels() {
            // given
            User user = User.createWithoutProfile("test@naver.com", "테스터", "123456");
            int channelCount = 7;
            ChannelTestUtils.createChannelList(user, entityManager, channelCount);

            // when
            List<ChannelInfoQuery> result = channelRepository.findAllMyChannels(user.getId(), null);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(channelCount);
            assertThat(result).allSatisfy(channelInfoQuery -> {
                assertThat(channelInfoQuery.getPublicType() == ChannelType.PUBLIC ||
                        channelMemberRepository.existsByChannelIdAndUserId(
                                channelInfoQuery.getChannelId(),
                                user.getId()
                        )
                ).isTrue();
            });
        }

        @Test
        @DisplayName("""
                성공: 키워드와 함께 채널 목록을 조회하면
                 공개 채널과 내가 참여한 비공개 채널들 중에서
                 채널명에 키워드가 포함된 채널들만 조회된다
                """)
        void success_find_all_my_channels_with_keyword() {
            // given
            User user = User.createWithoutProfile("test@naver.com", "테스터", "123456");
            int channelCount = 20;
            ChannelTestUtils.createChannelList(user, entityManager, channelCount);
            String searchTxt = "비밀";

            // when
            List<ChannelInfoQuery> result = channelRepository.findAllMyChannels(user.getId(), searchTxt);

            // then
            assertThat(result).isNotNull();
            assertThat(result.size()).isLessThanOrEqualTo(channelCount);
            assertThat(result).allSatisfy(channelInfoQuery -> {
                assertThat(channelInfoQuery.getName().contains(searchTxt)).isTrue();
            });
        }
    }

    @Nested
    @DisplayName("id로 채널 단일 조회")
    class FindByChannelId {
        @Test
        @DisplayName("성공: 유효한 channelId가 들어올 경우 Channel정보가 정상 반환된다")
        void success_find_by_channelId() {
            // given
            User user = User.createWithoutProfile("test@naver.com", "테스터", "123456");
            List<Channel> channelList = ChannelTestUtils.createChannelList(user, entityManager, 10);
            Channel channel = channelList.get(0);

            // when
            ChannelInfoQuery result = channelRepository.findByChannelId(channel.getId());

            // then
            assertThat(result).isNotNull();
            assertThat(result.getChannelId()).isEqualTo(channel.getId());
            assertThat(result.getName()).isEqualTo(channel.getName());
            assertThat(result.getDescription()).isEqualTo(channel.getDescription());
            assertThat(result.getPublicType()).isEqualTo(channel.getPublicType());
        }
    }
}
