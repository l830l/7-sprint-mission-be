package com.sprint.mission.discodeit.domain.user.controller;

import com.sprint.mission.discodeit.domain.user.controller.docs.UserControllerDocs;
import com.sprint.mission.discodeit.domain.auth.dto.response.AvailabilityRes;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.user.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.domain.user.dto.request.UserFindPasswordReq;
import com.sprint.mission.discodeit.domain.user.dto.request.UserInfoReq;
import com.sprint.mission.discodeit.domain.user.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.domain.user.dto.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.domain.user.dto.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.domain.userstatus.dto.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.domain.user.facade.UserCreationFacade;
import com.sprint.mission.discodeit.domain.user.facade.UserDeleteFacade;
import com.sprint.mission.discodeit.domain.user.facade.UserDetailViewFacade;
import com.sprint.mission.discodeit.domain.user.facade.UserOverviewFacade;
import com.sprint.mission.discodeit.domain.user.facade.UserUpdateFacade;
import com.sprint.mission.discodeit.domain.userstatus.facade.UserStatusUpdateFacade;
import com.sprint.mission.discodeit.domain.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserCreationFacade userCreationFacade;
    private final UserOverviewFacade userOverviewFacade;
    private final UserDetailViewFacade userDetailViewFacade;
    private final UserUpdateFacade userUpdateFacade;
    private final UserDeleteFacade userDeleteFacade;
    private final UserStatusUpdateFacade userStatusUpdateFacade;
    private final UserService userService;

    //사용자 목록 조회
    @GetMapping
    public ResponseEntity<List<UserSimpleInfoRes>> getAllUsers() {
        return ResponseEntity.ok(userOverviewFacade.findAll());
    }

    //사용자 단일 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailInfoRes> getUserByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(userDetailViewFacade.findById(userId));
    }

    //회원 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDetailInfoRes> createUser(
            @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
            @RequestPart(value = "profile", required = false) MultipartFile profileFile) {
        BinaryContentCreateReq binaryContentCreateReq = BinaryContentMapper.toReqDto(profileFile);
        UserDetailInfoRes user = userCreationFacade.createUser(
                UserCreateReq.from(userInfoReq, binaryContentCreateReq));
        return ResponseEntity.created(URI.create("/api/users/" + user.userId())).body(user);
    }

    // 회원 수정
    @PutMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDetailInfoRes> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        BinaryContentCreateReq binaryContentCreateReq = BinaryContentMapper.toReqDto(profile);
        UserUpdateReq req = UserUpdateReq.from(userInfoReq, binaryContentCreateReq);
        UserDetailInfoRes res = userUpdateFacade.updateUser(userId, req);
        return ResponseEntity.ok(res);
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userDeleteFacade.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 유저 온라인 상태 업그레이드
    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserStatusSimpleViewRes> updateUserStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(userStatusUpdateFacade.update(userId));
    }

    //이메일 가입되어있는지
    @GetMapping("/duplication/email")
    public ResponseEntity<AvailabilityRes> isRegisteredEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.isRegisteredEmail(email));
    }

    //닉네임 가입되어있는지
    @GetMapping("/duplication/nickname")
    public ResponseEntity<AvailabilityRes> isRegisteredNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.isRegisteredNickname(nickname));
    }

    //메일로 가입했던 아이디 발송
    @PostMapping("/id")
    public ResponseEntity<Void> sendEmailId(@RequestParam String email) {
        userService.sendEmailId(email);
        return ResponseEntity.ok().build();
    }

    //메일로 임시 비밀번호 발송
    @PostMapping("/password/reset")
    public ResponseEntity<Void> sendEmailPasswordReset(@Valid @RequestBody UserFindPasswordReq req) {
        userService.sendEmailTemporaryPassword(req);
        return ResponseEntity.ok().build();
    }
}
