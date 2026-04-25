package com.sprint.mission.discodeit.domain.binarycontent.dto.request;

public record BinaryContentCreateReq(
        byte[] data,
        String name,
        String type,
        long size
) {

}
