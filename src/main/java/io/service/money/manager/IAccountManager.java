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

    Optional<Transfer> transfer(Transfer transfer);

    Optional<Transfer> transfer(long amount, String fromAccountID, String toAccountID);
}
