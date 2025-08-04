package ru.d2k.parkle.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRoleDtoTest {
    private final static UserRoleDto userRoleDto = new UserRoleDto("USER", 10);

    @DisplayName("Test on identity")
    @Test
    public void identityTest() {
        Assertions.assertEquals(userRoleDto, userRoleDto);
    }

    @DisplayName("Test on equals two objects with different links")
    @Test
    public void equalsTest() {
        UserRoleDto secondUserRoleDto = new UserRoleDto("USER", 10);

        Assertions.assertEquals(userRoleDto, secondUserRoleDto);
    }
}
