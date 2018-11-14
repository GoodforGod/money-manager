package io.service.money.config;

import com.google.inject.AbstractModule;
import io.javalin.Javalin;
import io.service.money.controller.AccountController;
import io.service.money.controller.TransferController;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class ControllerBindConfig extends AbstractModule {

    private final Javalin javalin = Javalin.create();

    @Override
    protected void configure() {
        bind(Javalin.class).toInstance(javalin);

        bind(AccountController.class);
        bind(TransferController.class);
    }
}
