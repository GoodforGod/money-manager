package io.service.money.config;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import io.javalin.Javalin;
import io.service.money.controller.AccountController;
import io.service.money.controller.TransferController;
import io.service.money.controller.routing.AccountRouting;
import io.service.money.controller.routing.IRouting;
import io.service.money.controller.routing.TransferRouting;

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

        Multibinder<IRouting> actionBinder = Multibinder.newSetBinder(binder(), IRouting.class);
        actionBinder.addBinding().to(AccountRouting.class);
        actionBinder.addBinding().to(TransferRouting.class);

        bind(AccountController.class);
        bind(TransferController.class);
    }
}
