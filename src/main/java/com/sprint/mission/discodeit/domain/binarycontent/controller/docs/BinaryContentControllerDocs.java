package com.sprint.mission.discodeit.domain.binarycontent.controller.docs;

import com.sprint.mission.discodeit.global.controller.docs.ResponseCode;
import com.sprint.mission.discodeit.domain.binarycontent.dto.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentControllerDocs {

}
