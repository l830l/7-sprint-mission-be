package com.sprint.mission.discodeit.domain.channelmember.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelMemberNotFoundException extends ChannelMemberException {

    public ChannelMemberNotFoundException(ErrorCode errorCode,
                                          Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelMemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
