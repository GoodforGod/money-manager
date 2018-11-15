package io.service.money.support;

import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.impl.TransferStorage;

import java.util.Optional;

/**
 * Same behavior but sleeps for 2 sec before transfer save to emulate computing
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class AwaitTransferStorage extends TransferStorage {

    public AwaitTransferStorage(TransferRepository repository) {
        super(repository);
    }

    @Override
    public Optional<Transfer> save(Transfer transfer) {
        try {
            Thread.sleep(2000);
            return super.save(transfer);
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }
}
