package com.example.catalog;

import java.util.*;
import java.util.function.Function;

public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    protected final Map<ID, T> entities = new HashMap<>();
    protected final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public T save(T entity) {
        ID id = idExtractor.apply(entity);
        entities.put(id, entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        ID id = idExtractor.apply(entity);
        entities.remove(id);
    }

    @Override
    public void deleteById(ID id) {
        entities.remove(id);
    }

    @Override
    public boolean existsById(ID id) {
        return entities.containsKey(id);
    }
}
