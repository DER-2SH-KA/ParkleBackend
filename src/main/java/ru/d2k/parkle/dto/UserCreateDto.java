package ru.d2k.parkle.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "UserCreateDto must have any role (by name)")
    private String roleName;

    @NotBlank(message = "UserCreateDto login can't be null or blank")
    @Size(
            min = 3,
            max = 50,
            message = "UserCreateDto login size must be between 3 and 50 symbols"
    )
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", message = "UserCreateDto login isn't allowed by regexp pattern")
    private String login;

    @NotBlank(message = "UserCreateDto email can't be null or blank")
    @Size(
            max = 320,
            message = "UserCreateDto email size must be lower then 320 symbols"
    )
    @Email(message = "UserCreateDto email is not valid")
    private String email;

    @NotBlank(message = "UserCreateDto password can't be null or blank")
    @Size(
            min = 8,
            max = 72,
            message = "UserCreateDto password length must be between 8 and 72"
    )
    private String password;

}
