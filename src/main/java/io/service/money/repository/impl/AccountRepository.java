package io.service.money.repository.impl;

import com.google.inject.Singleton;
import io.service.money.model.dao.Account;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountRepository extends InMemoryRepository<Account, String> {

}
