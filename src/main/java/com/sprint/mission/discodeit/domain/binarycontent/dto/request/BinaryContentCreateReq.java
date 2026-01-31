package com.sprint.mission.discodeit.domain.binarycontent.dto.request;

public record BinaryContentCreateReq(
        byte[] data,
        String fileName,
        String fileType
) {

}
