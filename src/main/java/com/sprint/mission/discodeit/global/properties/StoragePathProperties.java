package com.sprint.mission.discodeit.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.storage")
public record StoragePathProperties(
        S3 s3
) {
    public record S3(
            String path
    ) {
    }
}
