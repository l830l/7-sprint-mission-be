package com.sprint.mission.discodeit.domain.channelmember.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelMemberAlreadyExistsException extends ChannelMemberException {

    public ChannelMemberAlreadyExistsException(
            ErrorCode errorCode,
            Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelMemberAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
