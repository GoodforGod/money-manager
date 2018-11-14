package io.service.money.manager;

import io.service.money.manager.impl.AccountManager;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.AccountRepository;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.impl.AccountStorage;
import io.service.money.storage.impl.TransferStorage;
import io.service.money.subs.AwaitTransferStorage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class AccountManagerTests extends Assert {

    private IAccountStorage accountStorage;

    private AccountManager accountManager;

    /**
     * Same behavior but sleeps for 5 sec before transfer save
     * (ONLY FOR TEST)
     * @see AwaitTransferStorage
     */
    private AccountManager awaitAccountManager;

    public AccountManagerTests() {
        TransferRepository transferRepository = new TransferRepository();
        this.accountStorage = new AccountStorage(new AccountRepository());

        this.accountManager = new AccountManager(accountStorage, new TransferStorage(transferRepository));
        this.awaitAccountManager = new AccountManager(accountStorage, new AwaitTransferStorage(transferRepository));
    }

    @Test
    public void validTransfer() {
        Account accountFrom = new Account(100);
        Account accountTo = new Account();
        Transfer transfer = new Transfer(50, accountFrom.getId(), accountTo.getId());
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveFrom.isPresent());
        assertTrue(saveTo.isPresent());

        Optional<Account> account = accountManager.transfer(transfer);
        assertNotNull(account);
        assertTrue(account.isPresent());
        assertEquals(50, account.get().getBalance().longValue());
    }

    @Test
    public void accountFromNotExist() {
        Account accountTo = new Account();
        Transfer transfer = new Transfer(50, "", accountTo.getId());
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveTo.isPresent());

        Optional<Account> account = accountManager.transfer(transfer);
        assertNotNull(account);
        assertFalse(account.isPresent());
    }

    @Test
    public void accountToNotExist() {
        Account accountFrom = new Account(100);
        Transfer transfer = new Transfer(50, accountFrom.getId(), "");
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        assertTrue(saveFrom.isPresent());

        Optional<Account> account = accountManager.transfer(transfer);
        assertNotNull(account);
        assertFalse(account.isPresent());
    }

    @Test
    public void amountNotValid() {
        Account accountFrom = new Account(100);
        Account accountTo = new Account();
        Transfer transfer = new Transfer(-50, accountFrom.getId(), accountTo.getId());
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveFrom.isPresent());
        assertTrue(saveTo.isPresent());

        Optional<Account> account = accountManager.transfer(transfer);
        assertNotNull(account);
        assertFalse(account.isPresent());
    }

    @Test
    public void performPossibleLockTransfers() {
        Account accountFrom = new Account(100);
        Account accountTo = new Account(300);
        Transfer transferFrom1to2 = new Transfer(50, accountFrom.getId(), accountTo.getId());
        Transfer transferFrom2to1 = new Transfer(150, accountTo.getId(), accountFrom.getId());
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveFrom.isPresent());
        assertTrue(saveTo.isPresent());

        long start = System.currentTimeMillis();
        CompletableFuture<Optional<Account>> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom1to2));
        CompletableFuture<Optional<Account>> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom2to1));
        Optional<Account> accountFrom1to2 = futureFrom1to2.join();
        Optional<Account> accountFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(10, roundCycleInSec);

        assertNotNull(accountFrom1to2);
        assertTrue(accountFrom1to2.isPresent());
        assertEquals(200, accountFrom1to2.get().getBalance().longValue());

        assertNotNull(accountFrom2to1);
        assertTrue(accountFrom2to1.isPresent());
        assertEquals(200, accountFrom2to1.get().getBalance().longValue());
    }

    @Test
    public void performAsyncTransfers() {
        Account accountFrom = new Account(100);
        Account accountFrom2 = new Account(300);
        Account accountThirdParty = new Account();
        Transfer transferFrom1toTarget = new Transfer(50, accountFrom.getId(), accountThirdParty.getId());
        Transfer transferFrom2toTarget = new Transfer(150, accountFrom2.getId(), accountThirdParty.getId());
        Optional<Account> saveFrom1 = accountStorage.save(accountFrom);
        Optional<Account> saveFrom2 = accountStorage.save(accountFrom2);
        Optional<Account> saveTo = accountStorage.save(accountThirdParty);
        assertTrue(saveFrom1.isPresent());
        assertTrue(saveFrom2.isPresent());
        assertTrue(saveTo.isPresent());

        long start = System.currentTimeMillis();
        CompletableFuture<Optional<Account>> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom1toTarget));
        CompletableFuture<Optional<Account>> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom2toTarget));
        Optional<Account> accountFrom1to2 = futureFrom1to2.join();
        Optional<Account> accountFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        // Rough but suitable in this situation
        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(10, roundCycleInSec);

        assertNotNull(accountFrom1to2);
        assertTrue(accountFrom1to2.isPresent());
        assertEquals(200, accountFrom1to2.get().getBalance().longValue());

        assertNotNull(accountFrom2to1);
        assertTrue(accountFrom2to1.isPresent());
        assertEquals(200, accountFrom2to1.get().getBalance().longValue());
    }
}
