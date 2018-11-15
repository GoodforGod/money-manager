package io.service.money.config;

import com.google.inject.AbstractModule;
import io.service.money.manager.ITransferManager;
import io.service.money.manager.impl.TransferManager;
import io.service.money.storage.IAccountStorage;
import io.service.money.storage.ITransferStorage;
import io.service.money.storage.impl.AccountStorage;
import io.service.money.storage.impl.TransferStorage;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class ServiceBindConfig extends AbstractModule {

    @Override
    protected void configure() {
        install(new ControllerBindConfig());

        bind(ITransferManager.class).to(TransferManager.class);
        bind(IAccountStorage.class).to(AccountStorage.class);
        bind(ITransferStorage.class).to(TransferStorage.class);
    }
}
