package ru.d2k.parkle.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRoleDtoTest {
    @DisplayName("equals -- Two different objects with same field's values")
    @Test
    public void equals_TwoDifferentObjectsWithSameFields() {
        UserRoleDto firstUserRoleDto = new UserRoleDto("USER", 10);
        UserRoleDto secondUserRoleDto = new UserRoleDto("USER", 10);

        Assertions.assertEquals(firstUserRoleDto, secondUserRoleDto);
    }
}
