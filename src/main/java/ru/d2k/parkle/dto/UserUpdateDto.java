package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;

// TODO: пока забил, не пользуюсь
@Getter
@Setter
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto implements UserDtoInterface {

    @JsonProperty("id")
    @NotNull(message = "UserUpdateDto id can't be null")
    private UUID id;

    // Should not to updating in database and system. It's normal.
    @JsonProperty("login")
    @Size(min = 3, max = 50, message = "UserUpdateDto login size must be between 3 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", message = "UserUpdateDto login isn't allowed by regexp pattern")
    private String login;

    @JsonProperty("password")
    @Size(min = 8, max = 72, message = "UserUpdateDto password length must be between 8 and 72")
    @Pattern(regexp = "^[a-zA-Z0-9`=!@#$%^&*()_+№;:?\\-\\\\/|]{8,72}$",
            message = "UserCreateDto password isn't allowed by regexp pattern")
    private String password;
}