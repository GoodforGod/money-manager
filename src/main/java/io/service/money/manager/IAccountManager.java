package io.service.money.manager;

import io.service.money.model.dao.Transfer;

import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public interface IAccountManager {

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param transfer to execute
     * @return optional target account
     */
    Optional<Transfer> transfer(Transfer transfer);

    /**
     * Operations on accounts are consistent ONLY when modifier by this method
     *
     * @param amount        amount to transfer
     * @param fromAccountID transfer from
     * @param toAccountID   transfer target
     * @return optional target account
     */
    Optional<Transfer> transfer(long amount, String fromAccountID, String toAccountID);
}
