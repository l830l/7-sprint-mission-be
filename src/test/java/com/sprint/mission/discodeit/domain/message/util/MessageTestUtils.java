package com.sprint.mission.discodeit.domain.message.util;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageTestUtils {
    public static List<Message> createMessages(
            User speaker,
            Channel channel,
            TestEntityManager testEntityManager,
            int count) {
        EntityManager entityManager = testEntityManager.getEntityManager();
        List<Message> messagesList = new ArrayList<>();
        testEntityManager.persist(channel);
        testEntityManager.persist(speaker);
        testEntityManager.flush();

        for (int i = 1; i <= count; i++) {
            String content = "메세지 내용입니다 " + i;
            Message message = Message.create(channel, speaker, content, List.of());
            testEntityManager.persist(message);

            // createdAt 랜덤화 (최근 365일)
            LocalDateTime createdAt = LocalDateTime.now()
                    .minusDays((long) (Math.random() * 365))
                    .minusHours((long) (Math.random() * 24))
                    .minusMinutes((long) (Math.random() * 60));

            entityManager.createQuery("update Message m set m.createdAt = :createdAt where m.id = :id")
                    .setParameter("createdAt", createdAt)
                    .setParameter("id", message.getId())
                    .executeUpdate();
            messagesList.add(message);
        }
        testEntityManager.clear();

        return messagesList;
    }
}
