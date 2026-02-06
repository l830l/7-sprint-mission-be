package com.sprint.mission.discodeit.domain.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sprint.mission.discodeit.domain.message.entity.QMessage.message;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Message> findAllByCursor(MessageCursorQuery query) {
        List<Message> result = queryFactory
                .selectFrom(message)
                .where(cursorCondition(query), keywordCondition(query))
                .orderBy(message.createdAt.desc(), message.id.desc())
                .limit(query.limit() + 1)
                .fetch();

        boolean hasNext = result.size() > query.limit();
        if (hasNext) {
            result.remove(result.size() - 1);
        }

        return new SliceImpl<>(result, PageRequest.of(0, query.limit()), hasNext);
    }

    // 현재 커서 이후(또는 이전) 범위 조건: where 절
    private BooleanExpression cursorCondition(MessageCursorQuery query) {
        // 첫 페이지
        if (query.cursor() == null || query.after() == null) {
            return null;
        }

        return message.createdAt.lt(query.cursor())
                .or(message.createdAt.eq(query.cursor()).and(message.id.lt(query.after())));
    }

    // 키워드 조건
    private BooleanExpression keywordCondition(MessageCursorQuery query) {
        return query.keyword() == null ? null : message.content.containsIgnoreCase(query.keyword());
    }
}
