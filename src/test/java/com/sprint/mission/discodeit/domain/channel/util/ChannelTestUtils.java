package com.sprint.mission.discodeit.domain.channel.util;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChannelTestUtils {
    public static List<Channel> createChannelList(
            User user,
            TestEntityManager testEntityManager,
            int count) {
        EntityManager entityManager = testEntityManager.getEntityManager();
        List<Channel> channelList = new ArrayList<>();

        // user 저장
        testEntityManager.persist(user);
        testEntityManager.flush();

        // channel 저장
        ChannelMemberRole role = ChannelMemberRole.MEMBER;
        for (int i = 1; i <= count; i++) {
            ChannelType type = ChannelType.values()[(i - 1) % ChannelType.values().length];

            Channel channel = switch (type) {
                case PUBLIC -> Channel.createPublic("채널명" + i, "");
                case PRIVATE -> Channel.createPrivate();
            };

            testEntityManager.persist(channel);
            if (channel.getPublicType() == ChannelType.PRIVATE) {
                ChannelMember channelMember = ChannelMember.create(user, channel, role);
                role = (role == ChannelMemberRole.MEMBER) ? ChannelMemberRole.MANAGER : ChannelMemberRole.MEMBER;
                testEntityManager.persist(channelMember);
                testEntityManager.flush();
            }

            // createdAt 랜덤화 (최근 365일)
            LocalDateTime createdAt = LocalDateTime.now()
                    .minusDays((long) (Math.random() * 365))
                    .minusHours((long) (Math.random() * 24))
                    .minusMinutes((long) (Math.random() * 60));

            entityManager.createQuery("update Channel c set c.createdAt = :createdAt where c.id = :id")
                    .setParameter("createdAt", createdAt)
                    .setParameter("id", channel.getId())
                    .executeUpdate();
            channelList.add(channel);
        }
        testEntityManager.clear();

        // channel member 저장

        return channelList;
    }
}
