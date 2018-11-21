package io.service.money.controller.routing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.controller.TransferController;

/**
 * @see TransferController
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
@Singleton
public class TransferRouting implements IRouting {

    @Inject private Javalin rest;
    @Inject private TransferController controller;

    @Override
    public void handle() {
        rest.get("/transfer/all", ctx -> {
            ctx.contentType("application/json").result(controller.getAllTransfers(ctx));
        });

        rest.get("/transfer/:id", ctx -> {
            ctx.contentType("application/json").result(controller.getTransfer(ctx));
        });

        // Example: /transfer?amount=10&senderID=1&receiverID=2
        rest.put("/transfer", ctx -> {
            ctx.contentType("application/json").result(controller.computeTransfer(ctx));
        });
    }
}
