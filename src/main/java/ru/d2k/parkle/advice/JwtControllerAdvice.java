package ru.d2k.parkle.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.d2k.parkle.dto.ErrorResponseDto;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.exception.JwtNotIncludeUserLoginException;

@Slf4j
@RestControllerAdvice
public class JwtControllerAdvice {

    @ExceptionHandler({JwtNotExistInRequestException.class, JwtNotIncludeUserLoginException.class})
    public ResponseEntity<ErrorResponseDto> handleJwtException(RuntimeException ex) {
        log.error("JWT error. Message: {}", ex.getMessage());

        // return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(
                        "Браузер не отправил достаточно данных о вас для автоматического входа в систему!",
                        ex.getMessage()
                ));
    }

}
