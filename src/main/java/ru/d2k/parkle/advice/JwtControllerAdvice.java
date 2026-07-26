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

    @ExceptionHandler(JwtNotExistInRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtNotExistInRequestException(RuntimeException ex) {
        log.error("JWT not exist in request error. Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Браузер не отправил токен авторизации!", ex.getMessage()));
    }

    @ExceptionHandler(JwtNotIncludeUserLoginException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtNotIncludeUserLoginException(JwtNotIncludeUserLoginException ex) {
        log.error("JWT not include user login error. Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Браузер не отправил достаточно данных о вас для " +
                        "автоматического входа в систему!", ex.getMessage()));
    }
}