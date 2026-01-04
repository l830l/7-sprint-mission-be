package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

  // Slice -> PageResponse
  public static <T> PageResponse<T> fromSlice(Slice<T> slice, String nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext()
    );
  }

  // Page -> PageResponse
  public static <T> PageResponse<T> fromPage(
      Page<T> page,
      String nextCursor
  ) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext()
    );
  }
}
