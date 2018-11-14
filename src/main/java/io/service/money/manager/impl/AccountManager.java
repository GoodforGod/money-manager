package io.service.money.manager.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.manager.IAccountManager;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.ITransferStorage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountManager implements IAccountManager {

    private final Map<String, Object> lockMap = new ConcurrentHashMap<>();

    @Inject private IAccountStorage accountStorage;
    @Inject private ITransferStorage transferStorage;

    public Optional<Account> transfer(Transfer transfer) {
        return transfer(transfer.getAmount(), transfer.getFromAccountID(), transfer.getToAccountID());
    }

    public Optional<Account> transfer(long amount, String fromAccountID, String toAccountID) {
        if(!accountStorage.exist(fromAccountID) || !accountStorage.exist(toAccountID))
            return Optional.empty();

        final Object lock = lockMap.computeIfAbsent(fromAccountID, (k) -> new Object());
        synchronized (lock) {
            final Optional<Account> accountFrom = accountStorage.find(fromAccountID);
            if(!accountFrom.isPresent())
                return Optional.empty();

            final Optional<Account> accountTo = accountStorage.find(toAccountID);
            if(!accountTo.isPresent())
                return Optional.empty();

            final Optional<Transfer> transfer =  accountFrom.get().transfer(amount, toAccountID);
            if(!transfer.isPresent())
                return Optional.empty();

            accountTo.get().accept(transfer.get());

            accountStorage.save(accountFrom.get());
            accountStorage.save(accountTo.get());
            transferStorage.save(transfer.get());

            return accountTo;
        }
    }
}
