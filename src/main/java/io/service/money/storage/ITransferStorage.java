package io.service.money.storage;

import io.service.money.model.dao.Transfer;

import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public interface ITransferStorage extends IStorage<Transfer, String> {

    // Yeah better to do same into TransferRepository, assume that its there
    List<Transfer> findBySender(String accountId);
    List<Transfer> findByRecipient(String accountId);
}
