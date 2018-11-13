package io.service.money.repository;

import io.service.money.model.dao.BaseModel;

import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public interface IRepository<T extends BaseModel<ID>, ID> {

    T find(ID id);
    List<T> findAll();

    T save(T t);
    List<T> save(List<T> list);

    T delete(T t);
    T deleteById(ID id);
}
