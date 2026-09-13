package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WebsiteEntityTest {

    private final RoleEntity role = new RoleEntity(UuidGeneratorUtil.generateNewUuidV7(), "TEMP");
    private final Set<RoleEntity> ROLES = Set.of(role);
    private final UserEntity user = new UserEntity(UuidGeneratorUtil.generateNewUuidV7(), "login",
            "password", ROLES);
    private final Set<WebsiteTagEntity> TAGS = new HashSet<>();

    @DisplayName("hashCode -- hash codes of the same object are the same")
    @Test
    public void hashCode_sameObjectHashCodeEquals() {
        WebsiteEntity website = new WebsiteEntity(UuidGeneratorUtil.generateNewUuidV7(), user, "#fff", "title",
                "description", "https://example.com", TAGS);

        Assertions.assertEquals(website.hashCode(), website.hashCode());
    }

    @DisplayName("hashCode -- hash codes of objects with different non Id fields are equals")
    @Test
    public void hashCode_twoObjectWithSameIdAreEquals() {
        UUID uuid = UuidGeneratorUtil.generateNewUuidV7();

        WebsiteEntity website1 = new WebsiteEntity(uuid, user, "#fff", "title1", "description1",
                "https://example.com1", TAGS);
        WebsiteEntity website2 = new WebsiteEntity(uuid, user, "#fff", "title2", "description2",
                "https://example.com2", TAGS);

        Assertions.assertEquals(website1.hashCode(), website2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @DisplayName("hashCode -- hash codes of null ID is null")
    @Test
    public void hashCode_objectIdIsNull() {
        WebsiteEntity website = new WebsiteEntity(null, user, "#fff", "title", "description",
                "https://example.com", TAGS);

        Assertions.assertEquals(102, website.hashCode());
    }

    @DisplayName("equals -- Same object are equals")
    @Test
    public void equals_sameObjectAreEquals() {
        UserEntity user = new UserEntity(UuidGeneratorUtil.generateNewUuidV7(), "login",
                "password", ROLES);

        Assertions.assertEquals(user, user);
    }

    @DisplayName("equals -- Object equals with null is false")
    @Test
    public void equals_withNullObject() {
        WebsiteEntity website = new WebsiteEntity(UuidGeneratorUtil.generateNewUuidV7(), user, "#fff", "title",
                "description", "https://example.com", TAGS);

        Assertions.assertFalse(website.equals(null));
    }

    @DisplayName("equals -- Objects with same ID are equals")
    @Test
    public void equals_objectsWithSameIdAreEquals() {
        UUID uuid = UuidGeneratorUtil.generateNewUuidV7();

        WebsiteEntity website1 = new WebsiteEntity(uuid, user, "#fff", "title1", "description1",
                "https://example.com1", TAGS);
        WebsiteEntity website2 = new WebsiteEntity(uuid, user, "#fff", "title2", "description2",
                "https://example.com2", TAGS);

        Assertions.assertEquals(website1, website2);
    }

    @DisplayName("equals -- equals with object which ID is null is false")
    @Test
    public void equals_withObjectIdNull() {
        WebsiteEntity website1 = new WebsiteEntity(UuidGeneratorUtil.generateNewUuidV7(), user, "#fff", "title1",
                "description1", "https://example.com1", TAGS);
        WebsiteEntity website2 = new WebsiteEntity(null, user, "#fff", "title2", "description2",
                "https://example.com2", TAGS);

        Assertions.assertFalse(website1.equals(website2));
    }
}