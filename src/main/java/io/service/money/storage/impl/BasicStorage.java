package io.service.money.storage.impl;

import io.service.money.model.dao.BaseModel;
import io.service.money.repository.IRepository;
import io.service.money.storage.IStorage;
import io.service.money.util.BasicUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
abstract class BasicStorage<T extends BaseModel<ID>, ID> implements IStorage<T, ID> {

    final IRepository<T, ID> repository;

    BasicStorage(IRepository<T, ID> repository) {
        this.repository = repository;
    }

    boolean isIdValid(ID id) {
        return id != null;
    }

    boolean isValid(T t) {
        return t != null;
    }

    @Override
    public Optional<T> find(ID id) {
        return (isIdValid(id))
                ? Optional.ofNullable(repository.find(id))
                : Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> save(T t) {
        if(!isValid(t))
            return Optional.empty();

        repository.save(t.getId(), t);
        return Optional.of(t);
    }

    @Override
    public List<T> save(List<T> list) {
        if(BasicUtils.isEmpty(list))
            return Collections.emptyList();

        list.forEach(this::save);
        return list;
    }

    @Override
    public Optional<T> delete(T t) {
        return deleteById(t.getId());
    }

    @Override
    public Optional<T> deleteById(ID id) {
        return Optional.ofNullable(repository.deleteById(id));
    }
}
