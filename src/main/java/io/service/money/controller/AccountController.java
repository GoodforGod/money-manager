package io.service.money.controller;

import io.service.money.storage.impl.AccountStorage;

import static spark.Spark.get;
import static spark.Spark.put;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class AccountController extends BasicController {

    private AccountStorage accountStorage;

    public AccountController() {
        get("/account/balance/:id", (request, response) -> {
            return "";
        });

        put("/account", (request, response) -> {
            return "";
        });

        // Assuming that client make a fiat deposit into bank
        put("/account/:deposit", (request, response) -> {
            return "";
        });
    }
}
