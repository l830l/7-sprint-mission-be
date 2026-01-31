package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.exception.ErrorCode;

import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

    public ChannelNotFoundException(ErrorCode errorCode,
                                    Map<String, String> details) {
        super(errorCode, details);
    }

    public ChannelNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
