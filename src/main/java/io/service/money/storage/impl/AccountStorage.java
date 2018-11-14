package io.service.money.storage.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.model.dao.Account;
import io.service.money.repository.impl.AccountRepository;
import io.service.money.storage.IAccountStorage;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountStorage extends BasicStorage<Account, String> implements IAccountStorage {

    @Inject
    public AccountStorage(AccountRepository repository) {
        super(repository);
    }
}
