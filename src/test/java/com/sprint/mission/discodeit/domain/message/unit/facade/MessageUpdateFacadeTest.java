package com.sprint.mission.discodeit.domain.message.unit.facade;

import com.sprint.mission.discodeit.domain.BaseEntity;
import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageUpdateReq;
import com.sprint.mission.discodeit.domain.message.dto.response.MessageViewRes;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.facade.MessageUpdateFacade;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.binarycontent.fixture.BinaryContentFixture;
import com.sprint.mission.discodeit.domain.channel.fixture.ChannelFixture;
import com.sprint.mission.discodeit.domain.message.fixture.MessageFixture;
import com.sprint.mission.discodeit.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class MessageUpdateFacadeTest {
    @Mock
    private MessageService messageService;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private MessageUpdateFacade messageUpdateFacade;

    @Nested
    @DisplayName("메세지 수정")
    class UpdateMessageContent {
        @Test
        @DisplayName("성공: 유효한 파라미터가 들어올 경우 메세지가 수정된다")
        void success_message_update() {
            // given
            List<BinaryContent> oldAttachmentList = new ArrayList<>(List.of(
                    BinaryContentFixture.create(),
                    BinaryContentFixture.create(),
                    BinaryContentFixture.create()
            ));
            Message message = MessageFixture.create(
                    ChannelFixture.createPublicChannel(),
                    UserFixture.createWithoutProfile(),
                    "메세지 입력",
                    oldAttachmentList
            );
            MessageUpdateReq request = new MessageUpdateReq(
                    "수정된 내용",
                    List.of(oldAttachmentList.get(0).getId()),
                    List.of(new BinaryContentCreateReq(
                                    "newImg".getBytes(), "newImg.jpg", "image/jpg", 10L),
                            new BinaryContentCreateReq(
                                    "newImg2".getBytes(), "newImg2.jpg", "image/jpg", 15L))
            );

            List<BinaryContent> newAttachmentList = request.newAttachmentReqs().stream()
                    .map(BinaryContentFixture::create)
                    .collect(Collectors.toList());
            newAttachmentList.addAll(
                    request.keepAttachmentIds().stream().map(BinaryContentFixture::create).toList()
            );

            given(messageService.findById(message.getId())).willReturn(message);
            for (int i = 0; i < request.newAttachmentReqs().size(); i++) {
                given(binaryContentService.upload(request.newAttachmentReqs().get(i)))
                        .willReturn(newAttachmentList.get(i));
            }

            // when
            MessageViewRes result = messageUpdateFacade.updateMessage(message.getId(), request);

            // then
            int deletedSize = message.getAttachments().size() - request.keepAttachmentIds().size();
            then(messageService).should(times(1)).findById(message.getId());

            ArgumentCaptor<UUID> deleteCaptor = ArgumentCaptor.forClass(UUID.class);
            then(binaryContentService).should(times(deletedSize)).delete(deleteCaptor.capture());
            then(binaryContentStorage).should(times(deletedSize)).delete(deleteCaptor.capture());
            then(messageService).should(times(1)).update(
                    eq(message.getId()),
                    eq(request.content()),
                    argThat(list ->
                            list.size() == newAttachmentList.size()
                                    &&
                                    list.stream().map(BaseEntity::getId).toList().containsAll(
                                            newAttachmentList.stream().map(BaseEntity::getId).toList())
                    )
            );

            List<UUID> attachmentIdList = newAttachmentList.stream()
                    .map(BaseEntity::getId).collect(Collectors.toList());

            assertThat(result.attachmentDatas().stream()
                    .map(BinaryContentInfoRes::binaryContentId)
                    .toList())
                    .containsExactlyInAnyOrderElementsOf(attachmentIdList);
        }
    }
}
