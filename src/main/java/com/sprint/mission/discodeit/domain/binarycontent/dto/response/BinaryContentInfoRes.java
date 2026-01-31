package com.sprint.mission.discodeit.domain.binarycontent.dto.response;

import java.util.UUID;

public record BinaryContentInfoRes(
        UUID binaryContentId,
        String fileName,
        String fileType,
        long fileSize
) {

}
