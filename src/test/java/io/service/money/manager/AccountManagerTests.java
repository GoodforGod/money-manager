package io.service.money.manager;

import io.service.money.manager.impl.AccountManager;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.AccountRepository;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.impl.AccountStorage;
import io.service.money.storage.impl.TransferStorage;
import io.service.money.support.AwaitTransferStorage;
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

        Optional<Transfer> optionalTransfer = accountManager.transfer(transfer);
        assertNotNull(optionalTransfer);
        assertTrue(optionalTransfer.isPresent());
        assertEquals(50, optionalTransfer.get().getAmount());
        assertEquals(accountFrom.getId(), optionalTransfer.get().getFromAccountID());
        assertEquals(accountTo.getId(), optionalTransfer.get().getToAccountID());
    }

    @Test
    public void accountFromNotExist() {
        Account accountTo = new Account();
        Transfer transfer = new Transfer(50, "", accountTo.getId());
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveTo.isPresent());

        Optional<Transfer> optionalTransfer = accountManager.transfer(transfer);
        assertNotNull(optionalTransfer);
        assertFalse(optionalTransfer.isPresent());
    }

    @Test
    public void accountToNotExist() {
        Account accountFrom = new Account(100);
        Transfer transfer = new Transfer(50, accountFrom.getId(), "");
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        assertTrue(saveFrom.isPresent());

        Optional<Transfer> optionalTransfer = accountManager.transfer(transfer);
        assertNotNull(optionalTransfer);
        assertFalse(optionalTransfer.isPresent());
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

        Optional<Transfer> optionalTransfer = accountManager.transfer(transfer);
        assertNotNull(optionalTransfer);
        assertFalse(optionalTransfer.isPresent());
    }

    @Test
    public void performPossibleDeadlockTransfers() {
        Account accountFrom = new Account(100);
        Account accountTo = new Account(300);
        Transfer transferFrom1to2 = new Transfer(50, accountFrom.getId(), accountTo.getId());
        Transfer transferFrom2to1 = new Transfer(150, accountTo.getId(), accountFrom.getId());
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveFrom.isPresent());
        assertTrue(saveTo.isPresent());

        long start = System.currentTimeMillis();
        CompletableFuture<Optional<Transfer>> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom1to2));
        CompletableFuture<Optional<Transfer>> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom2to1));
        Optional<Transfer> doneTransferFrom1to2 = futureFrom1to2.join();
        Optional<Transfer> doneTransferFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        // Due to 5 sec compute for transfer
        // Rough but suitable in this situation
        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(10, roundCycleInSec);

        assertNotNull(doneTransferFrom1to2);
        assertTrue(doneTransferFrom1to2.isPresent());
        assertEquals(50, doneTransferFrom1to2.get().getAmount());
        assertEquals(accountFrom.getId(), doneTransferFrom1to2.get().getFromAccountID());
        assertEquals(accountTo.getId(), doneTransferFrom1to2.get().getToAccountID());

        assertNotNull(doneTransferFrom2to1);
        assertTrue(doneTransferFrom2to1.isPresent());
        assertEquals(150, doneTransferFrom2to1.get().getAmount());
        assertEquals(accountTo.getId(), doneTransferFrom2to1.get().getFromAccountID());
        assertEquals(accountFrom.getId(), doneTransferFrom2to1.get().getToAccountID());

        Optional<Account> account1 = accountStorage.find(accountFrom.getId());
        assertNotNull(account1);
        assertTrue(account1.isPresent());
        assertEquals(200, account1.get().getBalance().longValue());

        Optional<Account> account2 = accountStorage.find(accountTo.getId());
        assertNotNull(account2);
        assertTrue(account2.isPresent());
        assertEquals(200, account2.get().getBalance().longValue());
    }

    @Test
    public void performTransfersWithPossibleLocks() {
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
        CompletableFuture<Optional<Transfer>> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom1toTarget));
        CompletableFuture<Optional<Transfer>> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitAccountManager.transfer(transferFrom2toTarget));
        Optional<Transfer> transferFrom1to2 = futureFrom1to2.join();
        Optional<Transfer> transferFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        // Due to 5 sec compute for transfer
        // Rough but suitable in this situation
        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(10, roundCycleInSec);

        assertNotNull(transferFrom1to2);
        assertTrue(transferFrom1to2.isPresent());
        assertEquals(50, transferFrom1to2.get().getAmount());
        assertEquals(accountFrom.getId(), transferFrom1to2.get().getFromAccountID());
        assertEquals(accountThirdParty.getId(), transferFrom1to2.get().getToAccountID());

        assertNotNull(transferFrom2to1);
        assertTrue(transferFrom2to1.isPresent());
        assertEquals(150, transferFrom2to1.get().getAmount());
        assertEquals(accountFrom2.getId(), transferFrom2to1.get().getFromAccountID());
        assertEquals(accountThirdParty.getId(), transferFrom2to1.get().getToAccountID());

        Optional<Account> account1 = accountStorage.find(accountFrom.getId());
        assertNotNull(account1);
        assertTrue(account1.isPresent());
        assertEquals(50, account1.get().getBalance().longValue());

        Optional<Account> account2 = accountStorage.find(accountFrom2.getId());
        assertNotNull(account2);
        assertTrue(account2.isPresent());
        assertEquals(150, account2.get().getBalance().longValue());

        Optional<Account> accountThird = accountStorage.find(accountThirdParty.getId());
        assertNotNull(accountThird);
        assertTrue(accountThird.isPresent());
        assertEquals(200, accountThird.get().getBalance().longValue());
    }
}
