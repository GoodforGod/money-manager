package io.service.money.storage.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.ITransferStorage;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Transfer> findBySender(String accountId) {
        return findAll().stream()
                .filter(t -> t.getSenderID().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByRecipient(String accountId) {
        return findAll().stream()
                .filter(t -> t.getReceiverID().equals(accountId))
                .collect(Collectors.toList());
    }
}
