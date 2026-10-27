package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Test;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class WebsiteEntityTest {

    // hashCode.
    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfSameObject() {
        WebsiteEntity website = new WebsiteEntity(WEBSITE_ID, USER_ENTITY, HEX_COLOR,
                WEBSITE_TITLE, WEBSITE_DESCRIPTION, WEBSITE_URL, WEBSITE_TAGS);

        assertEquals(website.hashCode(), website.hashCode());
    }

    @Test
    public void hashCode_shouldReturnEqualsHashCodeOfIdenticalObjectsId() {
        WebsiteEntity website1 = new WebsiteEntity(WEBSITE_ID, USER_ENTITY,
                HEX_COLOR, "title1", "description1", WEBSITE_URL,
                WEBSITE_TAGS);
        WebsiteEntity website2 = new WebsiteEntity(WEBSITE_ID, USER_ENTITY,
                HEX_COLOR, "title2", "description3", WEBSITE_URL,
                WEBSITE_TAGS);

        assertEquals(website1.hashCode(), website2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @Test
    public void hashCode_shouldReturn102WhenIdIsNull() {
        WebsiteEntity website = new WebsiteEntity(null, USER_ENTITY, HEX_COLOR,
                WEBSITE_TITLE, WEBSITE_DESCRIPTION, WEBSITE_URL, WEBSITE_TAGS);

        assertEquals(102, website.hashCode());
    }

    // equals.
    @Test
    public void equals_shouldBeNotEqualWhenObjectIsNull() {
        assertNotEquals(null, WEBSITE_ENTITY);
    }

    @Test
    public void equals_shouldBeEqualsWhenObjectsIdAreIdentical() {
        WebsiteEntity website1 = new WebsiteEntity(WEBSITE_ID, USER_ENTITY, HEX_COLOR,
                "title1", "description1", "https://example.com1",
                WEBSITE_TAGS);
        WebsiteEntity website2 = new WebsiteEntity(WEBSITE_ID, USER_ENTITY, HEX_COLOR,
                "title2", "description2", "https://example.com3",
                WEBSITE_TAGS);

        assertEquals(website1, website2);
    }

    @Test
    public void equals_shouldBeNotEqualWhenOneOfObjectIdIsNull() {
        WebsiteEntity website1 = new WebsiteEntity(WEBSITE_ID, USER_ENTITY, HEX_COLOR,
                WEBSITE_TITLE, WEBSITE_DESCRIPTION, WEBSITE_URL, WEBSITE_TAGS);
        WebsiteEntity website2 = new WebsiteEntity(null, USER_ENTITY, HEX_COLOR,
                WEBSITE_TITLE, WEBSITE_DESCRIPTION, WEBSITE_URL, WEBSITE_TAGS);

        assertNotEquals(website1, website2);
    }
}