package io.service.money.manager.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.error.TransferException;
import io.service.money.manager.ITransferManager;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.ITransferStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class TransferManager implements ITransferManager {

    /**
     * AccountID -> Lock (Same lock for sender/receiver accounts)
     */
    private final Map<String, Object> lockMap = new HashMap<>();

    private IAccountStorage accountStorage;
    private ITransferStorage transferStorage;

    @Inject
    public TransferManager(IAccountStorage accountStorage, ITransferStorage transferStorage) {
        this.accountStorage = accountStorage;
        this.transferStorage = transferStorage;
    }

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param transfer to execute
     * @return transfer details
     */
    public Transfer transfer(Transfer transfer) throws TransferException {
        return transfer(transfer.getAmount(), transfer.getSenderID(), transfer.getReceiverID());
    }

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param amount        amount to transfer
     * @param senderID transfer from
     * @param receiverID   transfer target
     * @return transfer details
     */
    public Transfer transfer(long amount, String senderID, String receiverID) throws TransferException {
        if (amount < 1)
            throw new TransferException("Incorrect transfer amount");

        synchronized (acquireLock(senderID, receiverID)) {
            final Account sender = accountStorage.find(senderID)
                    .orElseThrow(() -> new TransferException("Sender account does not exist"));

            final Account receiver = accountStorage.find(receiverID)
                    .orElseThrow(() -> new TransferException("Receiver account does not exist"));

            final Transfer transfer = sender.transfer(amount, receiverID)
                    .orElseThrow(() -> new TransferException("Not enough balance, can not perform transfer"));

            receiver.accept(transfer);

            accountStorage.save(sender);
            accountStorage.save(receiver);
            transferStorage.save(transfer);

            releaseLock(senderID, receiverID);
            return transfer;
        }
    }

    private synchronized Object acquireLock(String fromID, String toID) {
        final Object fromLock = lockMap.get(fromID);
        final Object toLock = lockMap.get(toID);

        if (fromLock == null && toLock == null) {
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
