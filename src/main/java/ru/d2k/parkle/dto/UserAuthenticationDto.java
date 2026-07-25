package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationDto implements UserDtoInterface {

    @NotBlank(message = "UserAuthenticationDto login must not be null or blank")
    @Size(min = 3, max = 50, message = "UserAuthenticationDto login length must be between 3 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,50}$", message = "UserAuthenticationDto login isn't allowed by regexp pattern")
    private String login;

    @NotBlank(message = "UserAuthenticationDto password must not be null or blank")
    @Size(min = 8, max = 72, message = "UserAuthenticationDto password lengths must be between 8 and 72 symbols")
    private String password;
}