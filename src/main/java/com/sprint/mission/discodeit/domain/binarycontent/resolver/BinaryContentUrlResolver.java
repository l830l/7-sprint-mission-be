package com.sprint.mission.discodeit.domain.binarycontent.resolver;

import java.util.UUID;

public interface BinaryContentUrlResolver {
    String resolve(UUID binaryContentId);
}
