package com.sprint.mission.discodeit.domain.binarycontent.storage.mock;

import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "mock")
public class MockBinaryContentStorage implements BinaryContentStorage {
    @Override
    public UUID put(UUID binaryId, byte[] data) {
        return null;
    }

    @Override
    public InputStream get(UUID binaryId) {
        return null;
    }

    @Override
    public Resource download(UUID binaryId) {
        return null;
    }

    @Override
    public void delete(UUID binaryId) {

    }
}
