package ru.d2k.parkle.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.d2k.parkle.dto.ErrorResponseDto;

@RestControllerAdvice
public class AuthenticationControllerAdvice {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> badCredentials(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto(
                        "Пользователь не существует или введены неверные данные",
                        ex.getMessage()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponseDto(
                        "Переданы неподходящие данные",
                        ex.getMessage()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }
}
