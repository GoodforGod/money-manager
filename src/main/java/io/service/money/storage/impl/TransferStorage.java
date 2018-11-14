package io.service.money.storage.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.ITransferStorage;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class TransferStorage extends BasicStorage<Transfer, String> implements ITransferStorage {

    @Inject
    public TransferStorage(TransferRepository repository) {
        super(repository);
    }
}
