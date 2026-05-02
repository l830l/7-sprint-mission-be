package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.properties.AdminInitializerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdminInitializerProperties.class)
public class AdminInitialConfig {
}