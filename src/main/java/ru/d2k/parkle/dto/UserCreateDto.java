package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto implements UserDtoInterface {

    @JsonProperty("login")
    @NotBlank(message = "UserCreateDto login can't be null or blank")
    @Size(min = 3, max = 50, message = "UserCreateDto login size must be between 3 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", message = "UserCreateDto login isn't allowed by regexp pattern")
    private String login;

    @JsonProperty("password")
    @NotBlank(message = "UserCreateDto password can't be null or blank")
    @Size(min = 8, max = 72, message = "UserCreateDto password length must be between 8 and 72")
    @Pattern(regexp = "^[a-zA-Z0-9`=!@#$%^&*()_+№;:?\\-\\\\/|]{8,72}$",
            message = "UserCreateDto password isn't allowed by regexp pattern")
    private String password;

    @JsonProperty("roles")
    @NotNull(message = "UserCreateDto roles cannot be null")
    @NotEmpty(message = "UserCreateDto roles cannot be null")
    private Set<RoleResponseDto> roles;
}