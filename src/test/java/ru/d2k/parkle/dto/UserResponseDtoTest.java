package ru.d2k.parkle.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class UserResponseDtoTest {

    private static final UUID UUID = UuidGeneratorUtil.generateNewUuidV7();
    private static final String LOGIN = "login";
    private static final Set<RoleResponseDto> USER_ROLES = Set.of(new RoleResponseDto(UuidGeneratorUtil.generateNewUuidV7(), "ROLE"));

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("equals/hashCode - Should be True with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UserResponseDto dto1 = new UserResponseDto(UUID, LOGIN, USER_ROLES);
        UserResponseDto dto2 = new UserResponseDto(UUID, LOGIN, USER_ROLES);

        Assertions.assertEquals(dto1.hashCode(), dto2.hashCode());
        Assertions.assertEquals(dto1, dto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        UserResponseDto dto = new UserResponseDto(UUID, LOGIN, USER_ROLES);

        Assertions.assertFalse(dto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        UserResponseDto dto = new UserResponseDto(UUID, LOGIN, USER_ROLES);

        Assertions.assertNotEquals(dto, new UserAuthenticationDto(null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(UserResponseDto dto2) {
        UserResponseDto dto1 = new UserResponseDto(UUID, LOGIN, USER_ROLES);

        Assertions.assertNotEquals(dto1, dto2);
    }

    private static Stream<UserResponseDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new UserResponseDto(UuidGeneratorUtil.generateNewUuidV7(), LOGIN, USER_ROLES),
                new UserResponseDto(UUID, "login2", USER_ROLES));
    }
}
