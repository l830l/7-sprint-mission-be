package domain.user.fixture;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFixture {
    private static long userCount = 1;

    // 기본 프로필 유저
    public static User createWithoutProfile() {
        User newUser = User.createWithoutProfile(
                "defaultTester" + userCount + "@aaa.com",
                "테스터" + userCount,
                "qwer123$"
        );
        ReflectionTestUtils.setField(newUser, "id", UUID.randomUUID());
        userCount++;
        return newUser;
    }

    // 커스텀 프로필 유저
    public static User createWithProfile() {
        BinaryContent emptyProfile = new BinaryContent(
                "emptyProfile" + userCount,
                ".jpg",
                10L
        );
        ReflectionTestUtils.setField(emptyProfile, "id", UUID.randomUUID());

        User newUser = User.createWithProfile(
                "profileTester" + userCount + "@aaa.com",
                "테스터" + userCount,
                "qwer123$",
                emptyProfile
        );
        ReflectionTestUtils.setField(newUser, "id", UUID.randomUUID());

        userCount++;
        return newUser;
    }

    // 유저 리스트
    public static List<User> createMixedList(int withProfileCount, int withoutProfileCount) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < withProfileCount; i++) {
            users.add(createWithProfile());
        }

        for (int i = 0; i < withoutProfileCount; i++) {
            users.add(createWithoutProfile());
        }

        return users;
    }

    public static List<User> createMixedList() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            users.add(createWithProfile());
        }

        for (int i = 0; i < 6; i++) {
            users.add(createWithoutProfile());
        }

        return users;
    }
}
