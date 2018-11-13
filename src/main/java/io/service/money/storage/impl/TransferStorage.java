package io.service.money.storage.impl;

import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class TransferStorage extends BasicStorage<Transfer, String> {

    public TransferStorage(TransferRepository repository) {
        super(repository);
    }
}
