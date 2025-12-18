package ru.d2k.parkle.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.d2k.parkle.dto.ErrorResponseDto;
import ru.d2k.parkle.exception.*;

@Slf4j
@RestControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoleNotFoundException(RuntimeException ex) {
        log.error("Role entity not found! Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Роль для пользователя не найдена!", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(RuntimeException ex) {
        log.error("User entity not found! Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Пользователь не найден!", ex.getMessage()));
    }

    @ExceptionHandler(WebsiteNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleWebsiteNotFoundException(RuntimeException ex) {
        log.error("Website entity not found! Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Интернет-ресурс не найден!", ex.getMessage()));
    }

    @ExceptionHandler(UserWrongPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleUserWrongPasswordException(RuntimeException ex) {
        log.error("User not found with this login and password. Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Пользователь с такими логином и/или паролем не найден!", ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(RuntimeException ex) {
        log.error("User not found with this username. Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Пользователь с таким логином не найден!", ex.getMessage()));
    }

    @ExceptionHandler(WebsiteIsExtremismSourceException.class)
    public ResponseEntity<ErrorResponseDto> handleExtremismSourceException(RuntimeException ex) {
        log.error("Website with this URL contains in extremism list. Message: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponseDto("Данная ссылка находится в списке экстремистских материалов!", ex.getMessage()));
    }

}
