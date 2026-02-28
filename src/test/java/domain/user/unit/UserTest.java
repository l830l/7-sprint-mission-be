package domain.user.unit;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Nested
    @DisplayName("유저 생성")
    class CreateUserTest {
        @Test
        @DisplayName("성공: 유효한 이메일과 닉네임, 비밀번호가 주어지면 User가 생성된다")
        void success_create_user_without_profile() {
            // given
            String email = "tester@aaa.com";
            String name = "테스터";
            String password = "qwer123$";

            // when
            User user = User.createWithoutProfile(email, name, password);

            // then
            assertThat(user).isNotNull();
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getNickname()).isEqualTo(name);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getProfile()).isNull();
        }

        @Test
        @DisplayName("성공: 유효한 이메일과 닉네임, 비밀번호, 프로필이 주어지면 user가 생성된다")
        void success_create_user_with_profile() {
            // given
            String email = "tester@aaa.com";
            String name = "테스터";
            String password = "qwer123$";
            BinaryContent emptyProfile = BinaryContent.create(
                    "profile",
                    ".jpg",
                    100L
            );

            // when
            User user = User.createWithProfile(email, name, password, emptyProfile);

            // then
            assertThat(user).isNotNull();
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getNickname()).isEqualTo(name);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getProfile()).isEqualTo(emptyProfile);
        }
    }
}
