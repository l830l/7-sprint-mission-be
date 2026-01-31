package com.sprint.mission.discodeit.global.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,        // 페이지 네이션의 컨텐츠
        String nextCursor,      // 다음 커서의 유무
        int size,               // 페이지의 사이즈
        boolean hasNext        // 다음 페이지가 있는지
) {

}
