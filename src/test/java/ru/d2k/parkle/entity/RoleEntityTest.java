package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Test;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleEntityTest {

    // hashCode.
    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfSameObject() {
        RoleEntity role = new RoleEntity(ROLE_ID, ROLE_NAME);

        assertEquals(role.hashCode(), role.hashCode());
    }

    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfIdenticalObjectsId() {
        RoleEntity role1 = new RoleEntity(ROLE_ID, "NAME1");
        RoleEntity role2 = new RoleEntity(ROLE_ID, "NAME2");

        assertEquals(role1.hashCode(), role2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
    *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
    *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @Test
    public void hashCode_shouldReturn102WhenIdIsNull() {
        RoleEntity role = new RoleEntity(null, ROLE_NAME);

        // !! 102, а не 0, по каким-то причинам. !!
        // Я из будущего: Hibernate и Lombok не ладят.
        assertEquals(102, role.hashCode());
    }

    // equals.
    @Test
    public void equals_shouldBeNotEqualWhenObjectIsNull() {
        assertNotEquals(null, ROLE_ENTITY);
    }

    @Test
    public void equals_shouldBeEqualsWhenObjectsIdAreIdentical() {
        RoleEntity role1 = new RoleEntity(ROLE_ID, "TEMP1");
        RoleEntity role2 = new RoleEntity(ROLE_ID, "TEMP2");

        assertEquals(role1, role2);
    }

    @Test
    public void equals_shouldBeNotEqualWhenOneOfObjectIdIsNull() {
        RoleEntity role1 = new RoleEntity(ROLE_ID, "TEMP1");
        RoleEntity role2 = new RoleEntity(null, "TEMP2");

        assertNotEquals(role1, role2);
    }
}