package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Context;
import io.service.money.manager.IAccountManager;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.ITransferStorage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class TransferController extends BasicController {

    @Inject private ITransferStorage transferStorage;
    @Inject private IAccountManager accountManager;

    public String getTransfer(Context context) {
        final ParseBox parseBox = getPathParam("id", context);
        if (parseBox.isEmpty())
            return convert(parseBox.getRestResponse());

        final Optional<Transfer> transfer = transferStorage.find(parseBox.getParam());
        return (!transfer.isPresent())
                ? errorResponse("Transfer is not presented")
                : validResponse(transfer.get());
    }

    public CompletableFuture<String> performTransfer(Context context) {
        final ParseBox parseBoxAmount = getPathParam("amount", context);
        final ParseBox parseBoxFromID = getPathParam("fromAccountID", context);
        final ParseBox parseBoxToID = getPathParam("toAccountID", context);
        final ParseBox errorParseBox = Stream.of(parseBoxAmount, parseBoxFromID, parseBoxToID)
                .filter(ParseBox::isEmpty)
                .findFirst()
                .orElse(null);

        if (errorParseBox != null)
            return CompletableFuture.completedFuture(convert(errorParseBox.getRestResponse()));

        final long amount = parseLongOrZero(parseBoxAmount.getParam());
        if (amount < 1)
            return CompletableFuture.completedFuture(errorResponse("Incorrect transfer amount"));

        return CompletableFuture.supplyAsync(() -> {
            final Transfer transfer = new Transfer(amount, parseBoxFromID.getParam(), parseBoxToID.getParam());
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
