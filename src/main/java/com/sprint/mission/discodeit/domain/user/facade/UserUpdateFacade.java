package com.sprint.mission.discodeit.domain.user.facade;

import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.binarycontent.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;

import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateFacade {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final BinaryContentStorage binaryContentStorage;

    //유저 수정
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #userId == @loginUser.userId()")
    public UserDetailInfoRes updateUser(UUID userId, UserUpdateReq req) {
        User user = userService.findById(userId);

        //이메일과 닉네임 부터 update(조건 맞지 않으면 바로 예외처리)
        userService.update(userId, req);

        //기존 프로필 사진 있으면 무조건 삭제
        if (user.getProfile() != null) {
            binaryContentService.delete(user.getProfile().getId());
            binaryContentStorage.delete(user.getProfile().getId());
            user.updateProfile(null);
        }

        //올라온 데이터가 있으면 무조건 만들어서 배정
        if (req.profileImage() != null) {
            BinaryContent profileImg = binaryContentService.upload(req.profileImage());
            user.updateProfile(profileImg);
            binaryContentStorage.put(profileImg.getId(), req.profileImage().data());
        }
        userStatusService.updateByUserId(userId);

        return UserMapper.toDetailResDto(
                user,
                user.getProfile() == null ?
                        null : BinaryContentMapper.toResDto(user.getProfile()),
                true
        );
    }
}
