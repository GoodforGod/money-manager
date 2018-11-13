package io.service.money.repository.impl;

import io.service.money.repository.IRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
abstract class InMemoryRepository<T, ID> implements IRepository<T, ID> {

    final Map<ID, T> memory = new ConcurrentHashMap<>();

    @Override
    public T find(ID id) {
        return memory.get(id);
    }

    @Override
    public List<T> findAll() {
        return memory.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public T save(ID id, T t) {
        memory.put(id, t);
        return t;
    }

    @Override
    public T deleteById(ID id) {
        return memory.remove(id);
    }
}
