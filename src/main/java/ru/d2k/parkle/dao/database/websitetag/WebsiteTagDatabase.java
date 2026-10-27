package ru.d2k.parkle.dao.database.websitetag;

import ru.d2k.parkle.dao.database.BasicCrudDatabase;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import java.util.List;
import java.util.UUID;

public interface WebsiteTagDatabase extends BasicCrudDatabase<WebsiteTagEntity, UUID> {

    List<WebsiteTagEntity> getByUserIdSortedByTitleAsc(UUID userId);
}
