package ru.d2k.parkle.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.UUID;

public class RoleResponseDtoTest {

    private static UUID ROLE_UUID = UuidGeneratorUtil.generateNewUuidV7();

    @DisplayName("equals -- Two different objects with same field's values")
    @Test
    public void equals_TwoDifferentObjectsWithSameFields() {
        RoleResponseDto firstRoleResponseDto = new RoleResponseDto(ROLE_UUID, "USER");
        RoleResponseDto secondRoleResponseDto = new RoleResponseDto(ROLE_UUID, "USER");

        assertEquals(firstRoleResponseDto, secondRoleResponseDto);
    }
}