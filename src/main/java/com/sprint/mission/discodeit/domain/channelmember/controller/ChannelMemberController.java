package com.sprint.mission.discodeit.domain.channelmember.controller;

import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.controller.docs.ChannelMemberControllerDocs;
import com.sprint.mission.discodeit.domain.channelmember.dto.request.ChannelMemberCreateReq;
import com.sprint.mission.discodeit.domain.channelmember.dto.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.domain.channelmember.facade.ChannerMemberCreateFacade;
import com.sprint.mission.discodeit.domain.channelmember.mapper.ChannelMemberMapper;
import com.sprint.mission.discodeit.domain.channelmember.service.ChannelMemberService;

import java.net.URI;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel-members")
@RequiredArgsConstructor
public class ChannelMemberController implements ChannelMemberControllerDocs {

    private final ChannerMemberCreateFacade channerMemberCreateFacade;
    private final ChannelMemberService channelMemberService;

    //메세지 수신 정보 생성
    @PostMapping
    public ResponseEntity<ChannelMemberInfoRes> createReadStatus(
            @RequestBody ChannelMemberCreateReq req) {
        ChannelMember channelMember = channerMemberCreateFacade.create(req);

        return ResponseEntity.created(URI.create("/api/channel-members/" + channelMember.getId()))
                .body(ChannelMemberMapper.toResDto(channelMember));
    }

    //메세지 수신 정보 업데이트
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ChannelMemberInfoRes> updateReadStatus(@PathVariable UUID readStatusId) {
        ChannelMember channelMember = channelMemberService.update(readStatusId);
        return ResponseEntity.ok(ChannelMemberMapper.toResDto(channelMember));
    }

    //메세지 수신 정보 조회
    @GetMapping("/{readStatusId}")
    public ResponseEntity<ChannelMemberInfoRes> getReadStatus(@PathVariable UUID readStatusId) {
        return ResponseEntity.ok(channelMemberService.findById(readStatusId));
    }

    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID readStatusId) {
        channelMemberService.delete(readStatusId);
        return ResponseEntity.noContent().build();
    }
}
