package ru.d2k.parkle.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;

/* TODO: Измени меня! Вместо кучи маленьких несколько важных тестов:
*   - Тестирование equals с похожими объектами;
*   - Параметризированное тестирование equals с разными полями объектов;
*   - Тестирование с Null объектами и полями в объектах;
*   - Тестирование через Jakarta Validation.
* */

public class RoleDtoTest {
    @DisplayName("equals - return true with same object")
    @Test
    public void shouldBeTrueWhenEqualsWithSameObject() {
        RoleDto roleDto = new RoleDto(Uuid7Generator.generateNewUUID(), "Role", 0);

        Assertions.assertTrue(roleDto.equals(roleDto));
    }

    @DisplayName("equals - return false with null")
    @Test
    public void shouldBeFalseWhenEqualsWithNull() {
        RoleDto roleDto = new RoleDto(Uuid7Generator.generateNewUUID(), "Role", 0);

        Assertions.assertFalse(roleDto.equals(null));
    }

    @DisplayName("equals - return false with another type of class")
    @Test
    public void shouldBeFalseWhenEqualsWithAnotherClassType() {
        RoleDto roleDto = new RoleDto(Uuid7Generator.generateNewUUID(), "Role", 0);

        Assertions.assertNotEquals(roleDto, new Object());
    }

    @DisplayName("equals - return false with object which has null fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichFieldsAreNull() {
        RoleDto roleDto = new RoleDto(Uuid7Generator.generateNewUUID(), "Role", 0);

        Assertions.assertNotEquals(roleDto, new RoleDto(null, null, null));
    }

    @DisplayName("hashCode/equals - return true with similar objects")
    @Test
    public void shouldBeTrueWhenEqualsAndHashCodeWithSimilarObjects() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        RoleDto roleDto1 = new RoleDto(uuid, "Role", 0);
        RoleDto roleDto2 = new RoleDto(uuid, "Role", 0);

        Assertions.assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
        Assertions.assertEquals(roleDto1, roleDto2);
    }

    @DisplayName("equals - return false with objects which has different fields")
    @Test
    public void shouldBeFalseWhenEqualsWithObjectWhichHasDiffFields() {
        RoleDto roleDto1 = new RoleDto(Uuid7Generator.generateNewUUID(), "Role1", 1);
        RoleDto roleDto2 = new RoleDto(Uuid7Generator.generateNewUUID(), "Role2", 2);

        Assertions.assertNotEquals(roleDto1, roleDto2);
    }

    @DisplayName("hashCode - return true with object which has all fields are null")
    @Test
    public void shouldBeEqualsWhenHashCodeWithObjectsWhichHasNullFields() {
        RoleDto roleDto1 = new RoleDto(null, null, null);
        RoleDto roleDto2 = new RoleDto(null, null, null);

        Assertions.assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
    }

    @DisplayName("hashCode - return true with object which has part of fields are null")
    @Test
    public void shouldBeEqualsWhenHashCodeWithObjectsWhichHasPartOfNullFields() {
        RoleDto roleDto1 = new RoleDto(null, "Role", null);
        RoleDto roleDto2 = new RoleDto(null, "Role", null);

        Assertions.assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
    }
}
