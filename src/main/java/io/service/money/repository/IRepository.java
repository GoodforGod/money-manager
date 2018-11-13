package io.service.money.repository;

import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public interface IRepository<T, ID> {

    T find(ID id);
    List<T> findAll();

    T save(ID id, T t);

    T deleteById(ID id);
}
