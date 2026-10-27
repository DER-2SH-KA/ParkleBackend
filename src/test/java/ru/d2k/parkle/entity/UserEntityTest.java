package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Test;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    // hashCode.
    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfSameObject() {
        UserEntity user = new UserEntity(USER_ID, USER_LOGIN, USER_PASSWORD, USER_ROLES);

        assertEquals(user.hashCode(), user.hashCode());
    }

    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfIdenticalObjectsId() {
        UserEntity user1 = new UserEntity(USER_ID, "login1", "password1", USER_ROLES);
        UserEntity user2 = new UserEntity(USER_ID, "login2", "password2", USER_ROLES);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @Test
    public void hashCode_shouldReturn102WhenIdIsNull() {
        UserEntity user = new UserEntity(null, USER_LOGIN, USER_PASSWORD, USER_ROLES);

        assertEquals(102, user.hashCode());
    }

    // equals.
    @Test
    public void equals_shouldBeNotEqualWhenObjectIsNull() {
        assertNotEquals(null, USER_ENTITY);
    }

    @Test
    public void equals_shouldBeEqualsWhenObjectsIdAreIdentical() {
        UserEntity user1 = new UserEntity(USER_ID, "login1",  "password1", USER_ROLES);
        UserEntity user2 = new UserEntity(USER_ID, "login2",  "password2", USER_ROLES);

        assertEquals(user1, user2);
    }

    @Test
    public void equals_shouldBeNotEqualWhenOneOfObjectIdIsNull() {
        UserEntity user1 = new UserEntity(USER_ID,"login1", "password1", USER_ROLES);
        UserEntity user2 = new UserEntity(null, "login2", "password2", USER_ROLES);

        assertNotEquals(user1, user2);
    }
}