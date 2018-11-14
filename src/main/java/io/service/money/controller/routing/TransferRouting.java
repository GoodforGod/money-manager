package io.service.money.controller.routing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.controller.TransferController;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
@Singleton
public class TransferRouting implements IRouting {

    @Inject private Javalin rest;
    @Inject private TransferController transferController;

    @Override
    public void handle() {
        rest.get("/transfer/:id", ctx -> {
            ctx.result(transferController.getTransfer(ctx));
        });

        rest.put("/transfer", ctx -> {
            ctx.result(transferController.performTransfer(ctx));
        });
    }
}
