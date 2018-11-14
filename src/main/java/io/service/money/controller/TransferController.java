package io.service.money.controller;

import io.service.money.storage.impl.TransferStorage;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class TransferController extends BasicController {

    private TransferStorage transferStorage;

    public TransferController() {
        get("/transfer/:id", (request, response) -> {
            return "";
        });

        // GET PARAMS
        get("/transfer", (request, response) -> {
            return "";
        });

        post("/transfer", (request, response) -> {
            return "";
        });
    }
}
