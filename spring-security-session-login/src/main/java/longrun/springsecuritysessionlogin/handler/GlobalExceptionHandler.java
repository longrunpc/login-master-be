package longrun.springsecuritysessionlogin.handler;

import longrun.springsecuritysessionlogin.dto.response.ErrorResponse;
import longrun.springsecuritysessionlogin.exception.BusinessException;
import longrun.springsecuritysessionlogin.exception.DuplicationException;
import longrun.springsecuritysessionlogin.exception.ErrorCode;
import longrun.springsecuritysessionlogin.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // validated 시 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
            break;// 일단 하나의 오류 정보만 담음
        }
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(String.valueOf(builder)).build());
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationException(DuplicationException e){
        final ErrorCode errorCode = e.getErrorCode();
        final String value = e.getValue();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .value(value)
                .build());
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
        final ErrorCode errorCode = e.getErrorCode();
        final String value = e.getValue();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                        .status(errorCode.getStatus())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .value(value)
                        .build());
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        final ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}



