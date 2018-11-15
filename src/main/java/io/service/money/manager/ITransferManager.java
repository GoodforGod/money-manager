package io.service.money.manager;

import io.service.money.error.TransferException;
import io.service.money.model.dao.Transfer;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public interface ITransferManager {

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param transfer to execute
     * @return transfer details
     */
    Transfer transfer(Transfer transfer) throws TransferException;

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param amount     amount to transfer
     * @param senderID   transfer from
     * @param receiverID transfer target
     * @return transfer details
     */
    Transfer transfer(long amount, String senderID, String receiverID) throws TransferException;
}
