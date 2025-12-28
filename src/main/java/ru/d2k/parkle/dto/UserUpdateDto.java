package ru.d2k.parkle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto implements UserDto {

    @Size(
            max = 32,
            message = "UserUpdateDto roleName must be not grater than 32 symbols"
    )
    private String roleName;

    @Size(
            min = 3,
            max = 50,
            message = "UserUpdateDto login size must be between 3 and 50 symbols"
    )
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", message = "UserUpdateDto login isn't allowed by regexp pattern")
    private String login;

    @Size(
            max = 320,
            message = "UserUpdateDto email size must be lower then 320 symbols"
    )
    @Email(message = "UserUpdateDto email is not valid")
    private String email;

    @Size(
            min = 8,
            max = 72,
            message = "UserUpdateDto password length must be between 8 and 72"
    )
    @Pattern(regexp = "^[a-zA-Z0-9`=!@#$%^&*()_+№;:?\\-\\\\/|]{8,72}$", message = "UserCreateDto password isn't allowed by regexp pattern")
    private String password;
}
