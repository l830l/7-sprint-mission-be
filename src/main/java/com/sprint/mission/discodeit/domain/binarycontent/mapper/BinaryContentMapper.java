package com.sprint.mission.discodeit.domain.binarycontent.mapper;

import com.sprint.mission.discodeit.domain.binarycontent.dto.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.FileConversionFail;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class BinaryContentMapper {
    public static BinaryContentCreateReq toReqDto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return new BinaryContentCreateReq(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );
        } catch (IOException e) {
            throw new FileConversionFail(ErrorCode.FILE_CONVERSION_FAILED);
        }
    }

    public static BinaryContentInfoRes toResDto(BinaryContent binaryContent) {
        if (binaryContent == null) {
            return null;
        } else {
            return new BinaryContentInfoRes(
                    binaryContent.getId(),
                    binaryContent.getName(),
                    binaryContent.getType(),
                    binaryContent.getSize()
            );
        }
    }
}
