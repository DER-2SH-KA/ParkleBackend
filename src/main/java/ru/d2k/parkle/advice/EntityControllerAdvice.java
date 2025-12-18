package ru.d2k.parkle.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.d2k.parkle.exception.*;

@Slf4j
@RestControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RuntimeException ex) {
        log.error("Role entity not found! Message: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(RuntimeException ex) {
        log.error("User entity not found! Message: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebsiteNotFoundException.class)
    public ResponseEntity<?> handleWebsiteNotFoundException(RuntimeException ex) {
        log.error("Website entity not found! Message: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO: избавиться ото всех try-catch, чтобы все ошибки всплывали вплоть до контроллеров!
    @ExceptionHandler({UserWrongPasswordException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleWrongPasswordException(RuntimeException ex) {
        log.error("User not found with this login and password. Message: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WebsiteIsExtremismSourceException.class)
    public ResponseEntity<?> handleExtremismSourceException(RuntimeException ex) {
        log.error("Website with this URL contains in extremism list. Message: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
