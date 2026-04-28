package com.sprint.mission.discodeit.domain.message.service;

import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.message.dto.query.MessageCursorQuery;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    // ===== Domain Logic (Facade 용)  =====
    //메세지를 id 로 참음
    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElseThrow(() ->
                new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    //메세지 생성
    @Override
    public Message create(Message message) {
        return messageRepository.save(message);
    }

    //메세지 수정
    @Override
    @Transactional
    public void update(UUID id, String content, List<BinaryContent> attachments) {
        Message message = findById(id);
        message.update(content, attachments);
    }

    //메세지 삭제
    @Override
    public void delete(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        messageRepository.deleteById(id);
    }

    // 메세지 조회
    @Override
    public Slice<Message> getMessages(MessageCursorQuery query) {
        return messageRepository.findAllByCursor(query);
    }
}