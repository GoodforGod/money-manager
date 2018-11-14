package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Account;
import io.service.money.storage.IAccountStorage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountController extends BasicController {

    @Inject private Javalin rest;
    @Inject private IAccountStorage accountStorage;

    @Override
    public void handle() {
        rest.get("/account/:id", ctx -> {
            ctx.result(CompletableFuture.supplyAsync(() -> {
                final ParseBox parseBox = getPathParam("id", ctx);
                if (parseBox.isEmpty())
                    return convert(parseBox.getRestResponse());

                final Optional<Account> account = accountStorage.find(parseBox.getParam());
                return (account.isPresent())
                        ? validResponse(account.get())
                        : errorResponse("Account does not exist");
            }));
        });

        rest.post("/account/create/:deposit", ctx -> {
            ctx.result(CompletableFuture.supplyAsync(() -> {
                final ParseBox parseBox = getPathParam("deposit", ctx);
                final long deposit = (parseBox.isEmpty())
                        ? parseLongOrZero(parseBox.getParam())
                        : 0;

                final Optional<Account> account = accountStorage.save(new Account(deposit));
                return (account.isPresent())
                        ? validResponse(account.get())
                        : errorResponse("Can not create account");
            }));
        });
    }

    private static long parseLongOrZero(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
