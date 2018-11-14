package io.service.money.storage;

import io.service.money.model.dao.BaseModel;

import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public interface IStorage<T extends BaseModel<ID>, ID> {

    boolean exist(ID id);

    Optional<T> find(ID id);
    List<T> findAll();

    Optional<T> save(T t);
    List<T> save(List<T> list);

    Optional<T> delete(T t);
    Optional<T> deleteById(ID id);
    void deleteAll();
}
