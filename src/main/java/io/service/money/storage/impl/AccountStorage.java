package io.service.money.storage.impl;

import io.service.money.model.dao.Account;
import io.service.money.repository.impl.AccountRepository;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class AccountStorage extends BasicStorage<Account, String> {

    public AccountStorage(AccountRepository repository) {
        super(repository);
    }
}
