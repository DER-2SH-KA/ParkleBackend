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
public class UserUpdateDto {
    private String roleName;

    @Size(
            max = 100,
            message = "UserUpdate login size must be lower then 100 symbols"
    )
    private String login;

    @NotBlank(message = "UserUpdate email can't be null or blank")
    @Size(
            max = 320,
            message = "UserUpdate email size must be lower then 320 symbols"
    )
    @Email(message = "UserUpdate email is not valid")
    private String email;

    @Size(
            min = 8,
            max = 72,
            message = "UserUpdate password length must be between 8 and 72"
    )
    private String password;
}
