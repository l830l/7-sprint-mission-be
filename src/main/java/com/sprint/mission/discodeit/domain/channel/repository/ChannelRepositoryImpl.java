package com.sprint.mission.discodeit.domain.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.entity.QChannel;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.channelmember.entity.QChannelMember;
import com.sprint.mission.discodeit.domain.message.entity.QMessage;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class ChannelRepositoryImpl implements ChannelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChannelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<ChannelInfoQuery> findAllMyChannels(UUID userId, String searchTxt) {
        QChannel ch = QChannel.channel;
        QChannelMember cm = QChannelMember.channelMember;
        QMessage m = QMessage.message;

        return queryFactory
                .select(Projections.constructor(ChannelInfoQuery.class,
                        ch.id,
                        ch.name,
                        ch.description,
                        ch.publicType,
                        queryFactory.select(cm.user.id)
                                .from(cm)
                                .where(cm.channel.eq(ch)
                                        .and(cm.role.eq(ChannelMemberRole.MANAGER))),
                        queryFactory.select(m.createdAt.max())
                                .from(m)
                                .where(m.channel.eq(ch))
                ))
                .from(ch)
                .where(
                        ch.publicType.eq(ChannelType.PUBLIC)
                                .or(ch.id.in(
                                        queryFactory.select(cm.channel.id)
                                                .from(cm)
                                                .where(cm.user.id.eq(userId))
                                )),
                        searchTxt != null ? ch.name.containsIgnoreCase(searchTxt) : null
                )
                .orderBy(ch.createdAt.desc())
                .fetch();
    }

    @Override
    public ChannelInfoQuery findByChannelId(UUID channelId) {
        QChannel ch = QChannel.channel;
        QChannelMember cm = QChannelMember.channelMember;
        QMessage m = QMessage.message;

        return queryFactory
                .select(Projections.constructor(ChannelInfoQuery.class,
                        ch.id,
                        ch.name,
                        ch.description,
                        ch.publicType,
                        queryFactory.select(cm.user.id)
                                .from(cm)
                                .where(cm.channel.eq(ch)
                                        .and(cm.role.eq(ChannelMemberRole.MANAGER))),
                        queryFactory.select(m.createdAt.max())
                                .from(m)
                                .where(m.channel.eq(ch))
                ))
                .from(ch)
                .where(ch.id.eq(channelId))
                .fetchOne();
    }
}
