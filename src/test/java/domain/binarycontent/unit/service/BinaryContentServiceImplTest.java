package domain.binarycontent.unit.service;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentServiceImpl;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import domain.binarycontent.fixture.BinaryContentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BinaryContentServiceImplTest {
    @Mock
    BinaryContentRepository binaryContentRepository;

    @Mock
    BinaryContentStorage binaryContentStorage;

    @InjectMocks
    BinaryContentServiceImpl binaryContentService;

    @Nested
    @DisplayName("바이너리 컨텐츠 업로드")
    class Upload {
        @Test
        @DisplayName("성공: 유효한 파일 정보가 들어오면 메타데이터를 DB에 저장하고, 생성된 ID를 파일이름으로 하여 데이터를 저장한다")
        void success_upload() {
            // given
            byte[] data = "test".getBytes();
            BinaryContentCreateReq req = new BinaryContentCreateReq(
                    data, "test_file", "image/png", 30L);
            BinaryContent binaryContent = BinaryContentFixture.create(req);

            given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);

            // when
            BinaryContent result = binaryContentService.upload(req);

            // then
            assertThat(result).isEqualTo(binaryContent);
            then(binaryContentRepository).should(times(1))
                    .save(any(BinaryContent.class));
            then(binaryContentStorage).should(times(1))
                    .put(eq(binaryContent.getId()), eq(data));
        }
    }
}
