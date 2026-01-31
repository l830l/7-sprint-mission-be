package com.sprint.mission.discodeit.domain.channelmember.entity;

import lombok.Getter;

@Getter
public enum ChannelMemberRole {
    MANAGER("manager"),
    MEMBER("member");

    private final String value;

    ChannelMemberRole(String value) {
        this.value = value;
    }
}
