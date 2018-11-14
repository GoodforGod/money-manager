package io.service.money.manager.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.manager.IAccountManager;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.ITransferStorage;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountManager implements IAccountManager {

    private final ConcurrentMap<String, Object> lockMap = new ConcurrentHashMap<>();

    private IAccountStorage accountStorage;
    private ITransferStorage transferStorage;

    @Inject
    public AccountManager(IAccountStorage accountStorage, ITransferStorage transferStorage) {
        this.accountStorage = accountStorage;
        this.transferStorage = transferStorage;
    }

    public Optional<Account> transfer(Transfer transfer) {
        return transfer(transfer.getAmount(), transfer.getFromAccountID(), transfer.getToAccountID());
    }

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param amount amount to transfer
     * @param fromAccountID transfer from
     * @param toAccountID transfer target
     * @return optional target account
     */
    public Optional<Account> transfer(long amount, String fromAccountID, String toAccountID) {
        if (!accountStorage.exist(fromAccountID) || !accountStorage.exist(toAccountID) || amount < 1)
            return Optional.empty();

        synchronized (acquireLock(fromAccountID, toAccountID)) {
            final Optional<Account> accountFrom = accountStorage.find(fromAccountID);
            if (!accountFrom.isPresent())
                return Optional.empty();

            final Optional<Account> accountTo = accountStorage.find(toAccountID);
            if (!accountTo.isPresent())
                return Optional.empty();

            final Optional<Transfer> transfer = accountFrom.get().transfer(amount, toAccountID);
            if (!transfer.isPresent())
                return Optional.empty();

            accountTo.get().accept(transfer.get());

            accountStorage.save(accountFrom.get());
            accountStorage.save(accountTo.get());
            transferStorage.save(transfer.get());

            releaseLock(fromAccountID, toAccountID);
            return accountTo;
        }
    }

    private synchronized Object acquireLock(String fromID, String toID) {
        final Object fromLock = lockMap.get(fromID);
        final Object toLock = lockMap.get(toID);

        if(fromLock == null && toLock == null) {
            Object newLock = new Object();
            lockMap.put(fromID, newLock);
            lockMap.put(toID, newLock);
            return newLock;
        }

        return (fromLock != null) ? fromLock : toLock;
    }

    private synchronized void releaseLock(String fromID, String toID) {
        lockMap.remove(fromID);
        lockMap.remove(toID);
    }
}
