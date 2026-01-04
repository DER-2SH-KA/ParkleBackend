package ru.d2k.parkle.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;
import java.util.stream.Stream;
import java.util.Set;

public class RoleDtoTest {
    private static final UUID uuid = Uuid7Generator.generateNewUUID();
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        RoleDto roleDto1 = new RoleDto(uuid, "Role", 1);
        RoleDto roleDto2 = new RoleDto(uuid, "Role", 1);

        Assertions.assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
        Assertions.assertEquals(roleDto1, roleDto2);
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        RoleDto roleDto = new RoleDto(uuid, "Role", 1);

        Assertions.assertFalse(roleDto.equals(null));
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        RoleDto roleDto = new RoleDto(uuid, "Role", 1);

        Assertions.assertNotEquals(roleDto, new RoleDto(null, null, null));
    }

    @DisplayName("equals - return false with objects which has different fields")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields(RoleDto roleDto2) {
        RoleDto roleDto1 = new RoleDto(uuid, "Role1", 1);

        Assertions.assertNotEquals(roleDto1, roleDto2);
    }

    @DisplayName("validate - return false with objects which has wrong field's values")
    @ParameterizedTest
    @MethodSource
    public void shouldBeFalseWhenValidationObjectHasWrongFieldsValues(RoleDto roleDto) {
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(roleDto);

        Assertions.assertFalse(violations.isEmpty());
    }

    @DisplayName("validate - return true with objects which has correct field's values")
    @Test
    public void shouldBeTrueWhenValidationObjectHasCorrectFieldsValues() {
        RoleDto roleDto = new RoleDto(uuid, "Developer", 1);
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(roleDto);

        Assertions.assertTrue(violations.isEmpty());
    }

    private static Stream<RoleDto> shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        return Stream.of(
                new RoleDto(Uuid7Generator.generateNewUUID(), "Role1", 1),
                new RoleDto(uuid, "Role2", 1),
                new RoleDto(uuid, "Role1", 2)
        );
    }

    private static Stream<RoleDto> shouldBeFalseWhenValidationObjectHasWrongFieldsValues() {
        return Stream.of(
                new RoleDto(uuid, null, 1),
                new RoleDto(uuid, "", 1),
                new RoleDto(uuid, "12345123451234512345123451234512345", 1),
                new RoleDto(uuid, "Role", null),
                new RoleDto(uuid, "Role", -1),
                new RoleDto(uuid, "Role", 0)
        );
    }
}
