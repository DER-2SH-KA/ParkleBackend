package ru.d2k.parkle.datasource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataProvider<C, E> {
    C create(E entity);
    List<C> getAll();
    Optional<C> getById(UUID id);
    C update(UUID id, E entityByUpdate);
    boolean deleteById(UUID id);
}
