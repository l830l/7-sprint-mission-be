package com.sprint.mission.discodeit.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.admin")
public record AdminInitializerProperties(
        String email,
        String nickname,
        String password
) {
}