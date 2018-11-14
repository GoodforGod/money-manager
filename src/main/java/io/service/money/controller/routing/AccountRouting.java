package io.service.money.controller.routing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.controller.AccountController;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
@Singleton
public class AccountRouting implements IRouting {

    @Inject private Javalin rest;
    @Inject private AccountController accountController;

    @Override
    public void handle() {
        rest.get("/account/:id", ctx -> {
            ctx.result(accountController.getAccount(ctx));
        });

        rest.post("/account/create/:deposit", ctx -> {
            ctx.result(accountController.createAccount(ctx));
        });
    }
}
