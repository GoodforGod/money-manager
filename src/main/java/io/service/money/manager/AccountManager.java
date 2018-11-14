package io.service.money.manager;

import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.impl.AccountStorage;
import io.service.money.storage.impl.TransferStorage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class AccountManager {

    private final Map<String, Object> lockMap = new ConcurrentHashMap<>();

    private AccountStorage accountStorage;
    private TransferStorage transferStorage;

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
