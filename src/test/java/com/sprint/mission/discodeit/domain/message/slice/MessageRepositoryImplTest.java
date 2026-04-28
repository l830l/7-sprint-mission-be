package com.sprint.mission.discodeit.domain.message.slice;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.message.util.MessageTestUtils;
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
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({TestQueryDslConfig.class, TestJpaAuditing.class})
public class MessageRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Nested
    @DisplayName("메세지 커서 페이지네이션 조회")
    class emptyListView {
        @Test
        @DisplayName("""
                    content는 빈 배열
                    hasNext는 false를 반환한다.
                """)
        void findAllByCursor_empty_Success() {
            // given
            User user = User.createWithoutProfile("tester@test.com", "테스터", "123456");
            Channel channel = Channel.createPrivate();
            MessageTestUtils.createMessages(
                    user, channel, entityManager, 0);
            MessageCursorQuery query = new MessageCursorQuery(
                    channel.getId(), null, null, 10, null);

            // when
            Slice<Message> result = messageRepository.findAllByCursor(query);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("limit 미만의 항목 수를 가진 리스트를 조회")
    class smallListView {
        @Test
        @DisplayName("""
                       content 는 모든 항목
                       hasNext는 false
                       를 반환한다
                """)
        void findAllByCursor_small_Success() {
            // given
            int totalCount = 15;
            User user = User.createWithoutProfile("tester@test.com", "테스터", "123456");
            Channel channel = Channel.createPrivate();
            MessageTestUtils.createMessages(
                    user, channel, entityManager, totalCount);
            MessageCursorQuery query = new MessageCursorQuery(
                    channel.getId(), null, null, 30, null);

            // when
            Slice<Message> result = messageRepository.findAllByCursor(query);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(totalCount);
            assertThat(result.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("limit 이상의 항목 수를 가진 리스트를 조회")
    class largeListView {
        @Test
        @DisplayName("""
                    각 페이지 정렬이 내림차순으로 잘 되어있고,
                    첫 페이지 첫 번째 요소 > 중간 페이지 첫 번째 요소 > 마지막 페이지 첫 번째 요소이며
                    첫번째 페이지는
                        content 는 전체의 일부항목
                        hasNext는 ture
                   중간 페이지는
                        content는 전체의 일부 항목
                        hasNext 는 true
                    마지막 페이지는
                        content는 전체의 남은 항목
                        hasNext 는 false
                    을 반환한다.
                """)
        void findMessage_large_Success() {
            // given
            int totalCount = 48;
            int pageSize = 20;
            User user = User.createWithoutProfile("tester@test.com", "테스터", "123456");
            Channel channel = Channel.createPrivate();
            MessageTestUtils.createMessages(
                    user, channel, entityManager, totalCount);

            // [Page 1]
            // given
            MessageCursorQuery query1 = new MessageCursorQuery(
                    channel.getId(),
                    null,
                    null,
                    pageSize,
                    null
            );

            // when
            Slice<Message> page1 = messageRepository.findAllByCursor(query1);

            // [Page 2]
            // given
            MessageCursorQuery query2 = new MessageCursorQuery(
                    channel.getId(),
                    page1.getContent().get(page1.getContent().size() - 1).getCreatedAt(),
                    page1.getContent().get(page1.getContent().size() - 1).getId(),
                    pageSize,
                    null
            );

            // when
            Slice<Message> page2 = messageRepository.findAllByCursor(query2);

            // [Page 3]
            // given
            MessageCursorQuery query3 = new MessageCursorQuery(
                    channel.getId(),
                    page2.getContent().get(page2.getContent().size() - 1).getCreatedAt(),
                    page2.getContent().get(page2.getContent().size() - 1).getId(),
                    pageSize,
                    null
            );

            // when
            Slice<Message> page3 = messageRepository.findAllByCursor(query3);

            // then
            // 각 페이지는 생성일 내림차순으로 정렬이 잘 되어있다.
            assertThat(page1.getContent()).isSortedAccordingTo(
                    Comparator.comparing(Message::getCreatedAt, Comparator.reverseOrder())
                            .thenComparing(Message::getId, Comparator.reverseOrder())
            );
            assertThat(page2.getContent()).isSortedAccordingTo(
                    Comparator.comparing(Message::getCreatedAt, Comparator.reverseOrder())
                            .thenComparing(Message::getId, Comparator.reverseOrder())
            );
            assertThat(page3.getContent()).isSortedAccordingTo(
                    Comparator.comparing(Message::getCreatedAt, Comparator.reverseOrder())
                            .thenComparing(Message::getId, Comparator.reverseOrder())
            );

            // 페이지들 간의 첫 요소 비교 (내림차순)
            assertThat(page1.getContent().get(0).getCreatedAt())
                    .isAfterOrEqualTo(page2.getContent().get(0).getCreatedAt());
            assertThat(page2.getContent().get(0).getCreatedAt())
                    .isAfterOrEqualTo(page3.getContent().get(0).getCreatedAt());

            // 각 페이지가 있는지 여부 검증
            assertThat(page1.hasNext()).isTrue();
            assertThat(page2.hasNext()).isTrue();
            assertThat(page3.hasNext()).isFalse();

            // 각 페이지 요소 수 검증
            assertThat(page1.getContent()).hasSize(pageSize);
            assertThat(page2.getContent()).hasSize(pageSize);
            assertThat(page3.getContent()).hasSize(totalCount % pageSize);
        }
    }

    @Nested
    @DisplayName("키워드로 조회")
    class findMessageByKeyword {
        @Test
        @DisplayName("성공: 키워드가 들어올 경우, 내용에 키워드가 포함된 메세지들만 조회된다")
        void success_find_message_by_keyword() {
            // given
            String keyword = "3";
            int messageCount = 100;
            User user = User.createWithoutProfile("tester@test.com", "테스터", "123456");
            Channel channel = Channel.createPrivate();
            MessageTestUtils.createMessages(
                    user, channel, entityManager, messageCount);
            MessageCursorQuery query = new MessageCursorQuery(
                    channel.getId(), null, null, 100, keyword);

            // when
            Slice<Message> result = messageRepository.findAllByCursor(query);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent().size()).isLessThanOrEqualTo(messageCount);
            assertThat(result.getContent()).allSatisfy(message ->
                    assertThat(message.getContent()).contains(keyword)
            );
        }
    }
}
