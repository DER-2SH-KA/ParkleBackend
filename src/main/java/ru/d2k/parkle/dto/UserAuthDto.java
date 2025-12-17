package ru.d2k.parkle.dto;

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
public class UserAuthDto {
    @NotBlank(message = "UserAuthDto login must not be null or blank")
    @Size(
            min = 3,
            max = 50,
            message = "UserAuthDto login length must be between 3 and 50 symbols"
    )
    @Pattern(regexp = "^[a-zA-Z0-9]{3,50}$", message = "UserAuthDto login isn't allowed by regexp pattern")
    private String login;

    @NotBlank(message = "UserAuthDto password must not be null or blank")
    @Size(
            min = 8,
            max = 72,
            message = "UserAuthDto password lengths must be between 8 and 72 symbols"
    )
    private String password;
}
