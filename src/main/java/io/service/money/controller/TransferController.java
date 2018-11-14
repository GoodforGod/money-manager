package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.service.money.manager.IAccountManager;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.model.dto.RestResponse;
import io.service.money.storage.ITransferStorage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class TransferController extends BasicController {

    @Inject private Javalin rest;
    @Inject private ITransferStorage transferStorage;
    @Inject private IAccountManager accountManager;

    @Override
    public void handle() {
        rest.get("/transfer/:id", ctx -> {
            ctx.result(CompletableFuture.supplyAsync(() -> {
                final ParseBox parseBox = getPathParam("id", ctx);
                if (parseBox.isEmpty())
                    return convert(parseBox.getRestResponse());

                final Optional<Transfer> transfer = transferStorage.find(parseBox.getParam());
                return (!transfer.isPresent())
                        ? errorResponse("Transfer is not presented")
                        : validResponse(transfer.get());
            }));
        });

        rest.put("/transfer", ctx -> {
            final ParseBox parseBoxAmount = getPathParam("amount", ctx);
            final ParseBox parseBoxFromID = getPathParam("fromAccountID", ctx);
            final ParseBox parseBoxToID = getPathParam("toAccountID", ctx);
            final RestResponse errorResponse = Stream.of(parseBoxAmount, parseBoxFromID, parseBoxToID)
                    .filter(ParseBox::isEmpty)
                    .findFirst()
                    .map((Function<ParseBox, RestResponse>) ParseBox::getRestResponse)
                    .orElse(null);

            if (errorResponse == null) {
                final long amount = parseLongOrZero(parseBoxAmount.getParam());
                if (amount == 0) {
                    ctx.result(errorResponse("Incorrect transfer amount"));
                } else {
                    Transfer transfer = new Transfer(amount, parseBoxFromID.getParam(), parseBoxToID.getParam());
                    ctx.result(computeTransfer(transfer));
                }
            } else {
                ctx.result(convert(errorResponse));
            }
        });

        rest.post("/transfer", ctx -> {

        });
    }

    private CompletableFuture<String> computeTransfer(final Transfer transfer) {
        return CompletableFuture.supplyAsync(() -> {
            final Optional<Account> transferCompleted = accountManager.transfer(transfer);
            return (transferCompleted.isPresent())
                    ? validResponse(transferCompleted.get())
                    : errorResponse("Could not complete transfer");
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
