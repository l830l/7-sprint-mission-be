package com.sprint.mission.discodeit.global.config;


import com.sprint.mission.discodeit.global.properties.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MultipartProperties.class)
public class MultipartConfig {
}
