package com.sprint.mission.discodeit.domain.userstatus.service;

import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusViewRes;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStateMissingException;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStateNotFoundException;
import com.sprint.mission.discodeit.domain.userstatus.mapper.UserStateMapper;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public UserStatus create(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUser_Id(userId).orElseThrow(
                () -> new UserStateMissingException(ErrorCode.USER_STATUS_MISSING)
        );
    }

    // ===== 🎯 Controller Direct (DTO 반환) =====
    @Override
    public UserStatusViewRes findById(@NonNull UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
                () -> new UserStateNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND)
        );
        return UserStateMapper.toDetailResDto(userStatus);
    }

    // ===== 🔧 Controller Direct (단일 도메인 / void) =====
    @Override
    @Transactional
    public void updateOfflineAt(@NonNull UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
                () -> new UserStateNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND)
        );
        userStatus.updateOfflineAt();
    }

    @Override
    @Transactional
    public void update(@NonNull UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
                () -> new UserStateNotFoundException(ErrorCode.USERSTATUS_NOT_FOUND)
        );
        userStatus.update();
    }

    @Override
    @Transactional
    public void updateByUserId(@NonNull UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        findByUserId(userId).update();
    }

    @Override
    public void delete(@NonNull UUID id) {
        userStatusRepository.deleteById(id);
    }
}
