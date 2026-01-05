package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.user.EmailInvalidException;
import com.sprint.mission.discodeit.exception.user.NicknameInvalidException;
import com.sprint.mission.discodeit.exception.user.PasswordInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a
                ));

        log.error("[ValidationError] {} | errors={} | url={} | method={} | ip={}",
                errorCode.getCode(),
                errors,
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode, errors));
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(DiscodeitException e,
                                                               HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[CustomException] {} - {} | url={} | method={} | ip={} \n",
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr()
                , e  // stack trace 포함
        );
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode, e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e,
                                                         HttpServletRequest request) {
        log.error("[Exception] {} | url={} | method={} | ip={}",
                e.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                request.getRemoteAddr()
                , e  // stack trace 포함
        );
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }
}
