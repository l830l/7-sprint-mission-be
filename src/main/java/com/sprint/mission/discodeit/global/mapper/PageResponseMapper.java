package com.sprint.mission.discodeit.global.mapper;

import com.sprint.mission.discodeit.domain.message.NextCursor;
import com.sprint.mission.discodeit.global.dto.response.PageResponse;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

    // Slice -> PageResponse
    public static <T> PageResponse<T> fromSlice(
            Slice<T> slice,
            NextCursor nextCursor
    ) {
        return new PageResponse<>(
                slice.getContent(),
                nextCursor.getCursor().toString(),
                nextCursor.getAfter().toString(),
                slice.getSize(),
                slice.hasNext()
        );
    }
}