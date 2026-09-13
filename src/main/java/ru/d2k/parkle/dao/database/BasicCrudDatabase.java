package ru.d2k.parkle.dao.database;

import java.util.Collection;
import java.util.Optional;

public interface BasicCrudDatabase<E, ID> {

    E save(E entity);
    Collection<E> getAll();
    Optional<E> getById(ID id);
    E getReferenceById(ID id);
    void deleteById(ID id);
    boolean existsById(ID id);
}
