package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d2k.parkle.controller.ApiRoutes;
import ru.d2k.parkle.dto.ErrorResponseDto;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.service.rest.AuthenticationService;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.AUTH_API)
public class AuthenticationRestController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserAuthenticationDto userAuthenticationDto,
                                          HttpServletResponse response) {
        UserResponseDto authenticatedUserDto = service.authenticate(userAuthenticationDto, response);

        return ResponseEntity.ok(authenticatedUserDto);
    }

    // TODO: Переделать с /{login} на /me
    @PatchMapping("/update/{login}")
    public ResponseEntity<?> updateByLogin(@PathVariable("login") String login,
                                           @Valid @RequestBody UserUpdateDto userUpdateDto,
                                           HttpServletResponse response) {
        UserResponseDto updatedUserDto = service.updateByLoginAndUpdateJwt(login, userUpdateDto, response);

        return ResponseEntity.ok(updatedUserDto);
    }

    // TODO: Переделать с /{login} на /me
    @DeleteMapping("/delete/{login}")
    public ResponseEntity<?> deleteByLogin(@PathVariable("login") String login, HttpServletResponse response) {
        boolean result = service.deleteByLoginAndUnauthorizeUser(login, response);

        return result ? ResponseEntity.ok().build() : (ResponseEntity.internalServerError()
                .body("User was not deleted!"));
    }

    @GetMapping("/isAuthed")
    public ResponseEntity<?> isUserAuthenticated() {
        Optional<UserResponseDto> userResponseDto = service.getUserResponseDtoFromSecurityContext();

        return userResponseDto.isPresent() ? ResponseEntity.ok(userResponseDto.get()) :
                    new ResponseEntity<>(new ErrorResponseDto("Пользователь не авторизован в системе",
                    "User not exists in system by jwt in request"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        service.logout(response);

        return ResponseEntity.ok().build();
    }
}