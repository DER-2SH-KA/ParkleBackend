package ru.d2k.parkle.dao.database.website;

import ru.d2k.parkle.dao.database.BasicCrudDatabase;
import ru.d2k.parkle.entity.WebsiteEntity;
import java.util.List;
import java.util.UUID;

public interface WebsiteDatabase extends BasicCrudDatabase<WebsiteEntity, UUID> {

    List<WebsiteEntity> getByUserIdSortedByTitleAsc(UUID userId);
}