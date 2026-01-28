package ru.d2k.parkle.datasource.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DatabaseProvider<E, ID> {
    E getReferenceById(ID id);
}
