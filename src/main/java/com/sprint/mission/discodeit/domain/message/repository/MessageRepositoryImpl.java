package com.sprint.mission.discodeit.domain.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.entity.QMessage;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //첫 페이지 반환
    @Override
    public Slice<Message> findFirstPage(UUID channelId, Pageable pageable) {
        QMessage m = QMessage.message;

        List<Message> results = queryFactory
                .selectFrom(m)
                .where(m.channel.id.eq(channelId))
                .orderBy(
                        m.createdAt.desc(),
                        m.id.desc()
                )
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return toSlice(results, pageable);
    }

    // 첫 페이지가 아닌 페이지
    @Override
    public Slice<Message> findNextPage(
            UUID channelId,
            LocalDateTime cursorCreatedAt,
            UUID cursorMessageId,
            Pageable pageable
    ) {
        QMessage m = QMessage.message;

        List<Message> results = queryFactory
                .selectFrom(m)
                .where(
                        m.channel.id.eq(channelId),
                        cursorCondition(m, cursorCreatedAt, cursorMessageId)
                )
                .orderBy(
                        m.createdAt.desc(),
                        m.id.desc()
                )
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return toSlice(results, pageable);
    }

    private BooleanExpression cursorCondition(
            QMessage m,
            LocalDateTime createdAt,
            UUID messageId
    ) {
        return m.createdAt.lt(createdAt)
                .or(
                        m.createdAt.eq(createdAt)
                                .and(m.id.lt(messageId))
                );
    }

    private Slice<Message> toSlice(List<Message> results, Pageable pageable) {
        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
