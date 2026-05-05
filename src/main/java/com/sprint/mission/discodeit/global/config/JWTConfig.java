package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JWTConfig {
}
