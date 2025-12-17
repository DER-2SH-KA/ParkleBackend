package ru.d2k.parkle.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.d2k.parkle.exception.*;

@ControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler({RoleNotFoundException.class, UserNotFoundException.class, WebsiteNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO: избавиться ото всех try-catch, чтобы все ошибки всплывали вплоть до контроллеров!
    @ExceptionHandler(UserWrongPasswordException.class)
    public ResponseEntity<?> handleWrongPasswordException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WebsiteIsExtremismSourceException.class)
    public ResponseEntity<?> handleExtremismSourceException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
