package io.service.money.manager;

import io.service.money.error.TransferException;
import io.service.money.manager.impl.TransferManager;
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
    private ITransferManager transferManager;

    /**
     * @see AwaitTransferStorage
     */
    private ITransferManager awaitTransferManager;

    public AccountManagerTests() {
        TransferRepository transferRepository = new TransferRepository();
        this.accountStorage = new AccountStorage(new AccountRepository());

        this.transferManager = new TransferManager(accountStorage, new TransferStorage(transferRepository));
        this.awaitTransferManager = new TransferManager(accountStorage, new AwaitTransferStorage(transferRepository));
    }

    @Test
    public void validTransfer() {
        Account sender = new Account(100);
        Account receiver = new Account();
        Transfer transfer = new Transfer(50, sender.getId(), receiver.getId());
        Optional<Account> savedSender = accountStorage.save(sender);
        Optional<Account> saveReceiver = accountStorage.save(receiver);
        assertTrue(savedSender.isPresent());
        assertTrue(saveReceiver.isPresent());

        Transfer optionalTransfer = transferManager.transfer(transfer);
        assertNotNull(optionalTransfer);
        assertEquals(50, optionalTransfer.getAmount());
        assertEquals(sender.getId(), optionalTransfer.getSenderID());
        assertEquals(receiver.getId(), optionalTransfer.getReceiverID());
    }

    @Test(expected = TransferException.class)
    public void senderNotExist() {
        Account receiver = new Account();
        Transfer transfer = new Transfer(50, "1", receiver.getId());
        Optional<Account> savedReceiver = accountStorage.save(receiver);
        assertTrue(savedReceiver.isPresent());

        Transfer optionalTransfer = transferManager.transfer(transfer);
        assertNotNull(optionalTransfer);
    }

    @Test(expected = TransferException.class)
    public void receiverNotExist() {
        Account sender = new Account(100);
        Transfer transfer = new Transfer(50, sender.getId(), "1");
        Optional<Account> savedSender = accountStorage.save(sender);
        assertTrue(savedSender.isPresent());

        Transfer optionalTransfer = transferManager.transfer(transfer);
        assertNotNull(optionalTransfer);
    }

    @Test(expected = TransferException.class)
    public void amountNotValid() {
        Account accountFrom = new Account(100);
        Account accountTo = new Account();
        Transfer transfer = new Transfer(-50, accountFrom.getId(), accountTo.getId());
        Optional<Account> saveFrom = accountStorage.save(accountFrom);
        Optional<Account> saveTo = accountStorage.save(accountTo);
        assertTrue(saveFrom.isPresent());
        assertTrue(saveTo.isPresent());

        Transfer optionalTransfer = transferManager.transfer(transfer);
        assertNotNull(optionalTransfer);
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
        CompletableFuture<Transfer> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitTransferManager.transfer(transferFrom1to2));
        CompletableFuture<Transfer> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitTransferManager.transfer(transferFrom2to1));
        Transfer doneTransferFrom1to2 = futureFrom1to2.join();
        Transfer doneTransferFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        // Due to 2 sec compute for transfer
        // Rough but suitable in this situation
        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(4, roundCycleInSec);

        assertNotNull(doneTransferFrom1to2);
        assertEquals(50, doneTransferFrom1to2.getAmount());
        assertEquals(accountFrom.getId(), doneTransferFrom1to2.getSenderID());
        assertEquals(accountTo.getId(), doneTransferFrom1to2.getReceiverID());

        assertNotNull(doneTransferFrom2to1);
        assertEquals(150, doneTransferFrom2to1.getAmount());
        assertEquals(accountTo.getId(), doneTransferFrom2to1.getSenderID());
        assertEquals(accountFrom.getId(), doneTransferFrom2to1.getReceiverID());

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
        CompletableFuture<Transfer> futureFrom1to2 = CompletableFuture.supplyAsync(() ->
                awaitTransferManager.transfer(transferFrom1toTarget));
        CompletableFuture<Transfer> futureFrom2to1 = CompletableFuture.supplyAsync(() ->
                awaitTransferManager.transfer(transferFrom2toTarget));
        Transfer transferFrom1to2 = futureFrom1to2.join();
        Transfer transferFrom2to1 = futureFrom2to1.join();
        long end = System.currentTimeMillis();

        // Due to 2 sec compute for transfer
        // Rough but suitable in this situation
        final long roundCycleInSec = Math.round((double) (end - start) / 1000);
        assertEquals(4, roundCycleInSec);

        assertNotNull(transferFrom1to2);
        assertEquals(50, transferFrom1to2.getAmount());
        assertEquals(accountFrom.getId(), transferFrom1to2.getSenderID());
        assertEquals(accountThirdParty.getId(), transferFrom1to2.getReceiverID());

        assertNotNull(transferFrom2to1);
        assertEquals(150, transferFrom2to1.getAmount());
        assertEquals(accountFrom2.getId(), transferFrom2to1.getSenderID());
        assertEquals(accountThirdParty.getId(), transferFrom2to1.getReceiverID());

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
