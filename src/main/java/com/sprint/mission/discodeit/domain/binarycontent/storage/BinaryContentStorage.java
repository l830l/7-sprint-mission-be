package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    UUID put(UUID binaryId, byte[] data);

    InputStream get(UUID binaryId);

    Resource download(UUID binaryId);

    void delete(UUID binaryId);
}
