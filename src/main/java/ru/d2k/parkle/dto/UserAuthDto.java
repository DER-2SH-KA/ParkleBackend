package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
    @NotBlank(message = "User's Auth login must not be null or blank")
    @Size(max = 100, message = "User's Auth login length must be lower than 100 symbols")
    private String login;

    @NotBlank(message = "User's Auth password must not be null or blank")
    @Size(
            min = 8,
            max = 72,
            message = "User's Auth password lengths must be between 8 and 72 symbols"
    )
    private String password;
}
