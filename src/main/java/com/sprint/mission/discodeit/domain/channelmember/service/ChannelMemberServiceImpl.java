package com.sprint.mission.discodeit.domain.channelmember.service;

import com.sprint.mission.discodeit.domain.channelmember.dto.response.ChannelMemberInfoRes;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMember;
import com.sprint.mission.discodeit.domain.channelmember.entity.ChannelMemberRole;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.channelmember.exception.ChannelMemberAlreadyExistsException;
import com.sprint.mission.discodeit.domain.channelmember.exception.ChannelMemberNotFoundException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.channelmember.mapper.ChannelMemberMapper;
import com.sprint.mission.discodeit.domain.channelmember.repository.ChannelMemberRepository;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelMemberServiceImpl implements ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public ChannelMember create(ChannelMember channelMember) {
        //유저id 랑 channel id 가 이미 있는 readStatus 가 있으면 에러
        if (channelMemberRepository.existsByChannelIdAndUserId(
                channelMember.getChannel().getId(), channelMember.getUser().getId()
        )) {
            throw new ChannelMemberAlreadyExistsException(ErrorCode.CHANNELMEMEBER_ALREADY_EXISTS);
        }

        return channelMemberRepository.save(channelMember);
    }

    @Override
    @Transactional
    public ChannelMember update(UUID id) {
        ChannelMember channelMember = channelMemberRepository.findById(id).orElseThrow(
                () -> new ChannelMemberNotFoundException(ErrorCode.CHANNELMEMEBER_NOT_FOUND)
        );
        channelMember.updateReadAt();
        return channelMember;
    }

    @Override
    public void delete(UUID id) {
        if (!channelMemberRepository.existsById(id)) {
            throw new ChannelMemberNotFoundException(ErrorCode.CHANNELMEMEBER_NOT_FOUND);
        }
        channelMemberRepository.deleteById(id);
    }

    @Override
    public ChannelMember findManagerByChannelId(UUID channelId) {
        return channelMemberRepository.findByChannelIdAndRole(channelId, ChannelMemberRole.MANAGER)
                .stream().findFirst().orElseThrow(
                        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
                );
    }

    @Override
    public List<ChannelMember> findMembersByChannelId(UUID channelId) {
        return channelMemberRepository.findByChannelIdAndRole(channelId, ChannelMemberRole.MEMBER)
                .stream().toList();
    }

    @Override
    public List<ChannelMember> findAllByChannelId(UUID channelId) {
        return channelMemberRepository.findAllByChannelId(channelId);
    }

    // ===== 🎯 Controller Direct (DTO 반환) =====
    @Override
    @Transactional(readOnly = true)
    public ChannelMemberInfoRes findById(UUID id) {
        ChannelMember channelMember = channelMemberRepository.findById(id).orElseThrow(() ->
                new ChannelMemberNotFoundException(ErrorCode.CHANNELMEMEBER_NOT_FOUND));
        return ChannelMemberMapper.toResDto(channelMember);
    }
}
