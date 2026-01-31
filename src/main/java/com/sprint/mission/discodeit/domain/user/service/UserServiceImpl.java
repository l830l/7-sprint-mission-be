package com.sprint.mission.discodeit.domain.user.service;

import com.sprint.mission.discodeit.global.email.EmailSender;
import com.sprint.mission.discodeit.domain.auth.dto.response.AvailabilityRes;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.user.exception.InvalidUserNicknameException;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    //리포지토리
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    // ===== 🎯 Controller Direct (DTO 반환) =====
    //메일로 가입했던 아이디를 발송
    public void sendEmailId(String email) {
        User user = findByEmail(email);
        emailSender.sendEmailAsync(
                email,
                "[ch-ah] 가입하신 아이디를 보내드립니다",
                "nickname: " + user.getNickname()
        );
    }

    //메일로 임시 비밀번호 발송 및 임시 비밀번호 발급
    @Override
    @Transactional
    public void sendEmailTemporaryPassword(String email, String nickname) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        if (!user.getNickname().equals(nickname)) {
            throw new InvalidUserNicknameException(ErrorCode.INVALID_USER_NICKNAME);
        }
        String passwordTemp = UUID.randomUUID().toString().replaceAll("-", "");
        user.updateTemporaryPassword(passwordTemp);
        emailSender.sendEmailAsync(
                email,
                "[ch-at] 임시 비밀번호를 보내드립니다",
                String.format("""
                        임시 비밀번호: %s
                        반드시 이후에 비밀번호 변경을 해주세요.""", passwordTemp)
        );
    }

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    //유저 추가
    @Override
    public User create(User user) {
        validateDuplicate(user.getEmail(), user.getNickname());
        return userRepository.save(user);
    }

    //유저 목록
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //유저 아이디로 조회
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    //닉네임으로 찾기
    @Override
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElse(null);
    }

    //삭제
    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    //업데이트
    @Override
    public void update(UUID id, UserUpdateReq req) {
        User user = findById(id);
        //기존 비밀번호를 클라이언트 쪽에서 알 수 없기 때문에, 새로 올라온 비밀번호가 없으면 비밀번호 변경X
        String replacaPassword = req.password() == null ? user.getPassword() : req.password();
        if (req.password() == null) {
            replacaPassword = user.getPassword();
        }
        validateDuplicate(id, req.email(), req.nickname());
        user.update(req.email(), req.nickname(), replacaPassword);
    }

    //해당 닉네임으로 가입된 사림이 있는지.
    @Override
    public AvailabilityRes isRegisteredNickname(String nickname) {
        return new AvailabilityRes(
                userRepository.existsByNickname(nickname));
    }

    //해당 이메일로 가입된 사림이 있는지.
    @Override
    public AvailabilityRes isRegisteredEmail(String email) {
        return new AvailabilityRes(
                userRepository.existsByEmail(email));
    }


    // ===== 🔒 Private Logic (내부 사용) =====
    // 신규 유저 중복 검사
    private void validateDuplicate(String email, String nickname) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new UserAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    //기존 유저 회원 정보 수정 시 중복 검사
    private void validateDuplicate(UUID userId, String email, String nickname) {
        if (userRepository.existsByIdNotAndEmail(userId, email)) {
            throw new UserAlreadyExistsException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByIdNotAndNickname(userId, nickname)) {
            throw new UserAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }
}
