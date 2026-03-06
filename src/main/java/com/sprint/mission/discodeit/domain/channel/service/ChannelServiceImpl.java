package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.dto.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.domain.channel.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepositoryCustom;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelPrivateCannotModifyException;
import com.sprint.mission.discodeit.domain.channel.factory.ChannelFactory;
import com.sprint.mission.discodeit.domain.channelmember.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

    //레포지토리
    private final ChannelRepository channelRepository;
    private final ChannelRepositoryCustom channelRepositoryCustom;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelFactory channelFactory;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    //채널 생성
    @Override
    public Channel create(ChannelCreateReq req) {
        return channelRepository.save(channelFactory.create(req));
    }

    //채널 생성: 비공개
    @Override
    public Channel create(ChannelCreateSecReq req) {
        return channelRepository.save(channelFactory.create(req));
    }

    //채널 삭제
    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        channelRepository.deleteById(id);
    }

    // 내가 참여한 채널 목록 조회
    @Override
    public List<ChannelInfoQuery> getAllByUser(UUID userId, String searchTxt) {
        return channelRepositoryCustom.findAllMyChannels(userId,
                searchTxt);
    }

    // 단일 채널 조회
    @Override
    public ChannelInfoQuery get(UUID channelId) {
        return channelRepositoryCustom.findByChannelId(channelId);
    }

    //채널 id 로 조회
    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id).orElseThrow(
                () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND)
        );
    }

    //채널 수정
    @Override
    @Transactional
    public Channel update(@NonNull UUID id, @NonNull ChannelUpdateReq req) {
        Channel channel = channelRepository.findById(id).orElseThrow(
                () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND)
        );
        if (channel.getPublicType() == ChannelType.PRIVATE) {
            throw new ChannelPrivateCannotModifyException(ErrorCode.CHANNEL_PRIVATE_CANNOT_MODIFY);
        }
        channel.update(req.name(), req.description());
        return channel;
    }
}
