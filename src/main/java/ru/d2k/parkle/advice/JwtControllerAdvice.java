package ru.d2k.parkle.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.exception.JwtNotIncludeUserLoginException;

@ControllerAdvice
public class JwtControllerAdvice {

    @ExceptionHandler({JwtNotExistInRequestException.class, JwtNotIncludeUserLoginException.class})
    public ResponseEntity<?> handleJwtException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
