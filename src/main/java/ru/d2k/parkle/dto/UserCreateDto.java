package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotNull(message = "UserCreate must have any role (by Name)")
    private String roleName;

    @NotBlank(message = "UserCreate's login can't be null or blank")
    @Size(
            max = 100,
            message = "UserCreate's login size must be lower then 100 symbols"
    )
    private String login;

    @NotBlank(message = "UserCreate's email can't be null or blank")
    @Size(
            max = 320,
            message = "UserCreate's email size must be lower then 320 symbols"
    )
    @Pattern(
            regexp = "^([a-zA-Z0-9-._]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)$",
            message = "UserCreate's email is not valid by pattern"
    )
    private String email;

    @NotBlank(message = "UserCreate's password can't be null or blank")
    @Size(
            min = 8,
            max = 72,
            message = "UserCreate's password length must be between 8 and 72"
    )
    private String password;

}
