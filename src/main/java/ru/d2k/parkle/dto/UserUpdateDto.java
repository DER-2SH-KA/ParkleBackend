package ru.d2k.parkle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private UUID roleId;

    @Size(
            max = 100,
            message = "UserUpdate login size must be lower then 100 symbols"
    )
    private String login;

    @Size(
            max = 320,
            message = "UserUpdate email size must be lower then 320 symbols"
    )
    @Pattern(
            regexp = "^([a-zA-Z0-9-._]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)$",
            message = "UserUpdate email is not valid by pattern"
    )
    private String email;

    @Size(
            min = 8,
            max = 72,
            message = "UserUpdate password length must be between 8 and 72"
    )
    private String password;
}
