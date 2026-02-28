package domain.user.fixture;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class UserFixture {
    private static int userCount = 1;

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
        BinaryContent emptyProfile = BinaryContent.create(
                "emptyProfile" + userCount,
                ".jpg",
                userCount
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
}
