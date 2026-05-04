package com.sprint.mission.discodeit.domain.user.facade;

import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeleteFacade {

    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    //유저 삭제
    @PreAuthorize("hasRole('ADMIN') or #userId == @loginUser.userId()")
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userService.findById(userId);

        if (user.getProfile() != null) {
            binaryContentService.delete(user.getProfile().getId());
            binaryContentStorage.delete(user.getProfile().getId());
        }
        userService.delete(userId);
    }
}