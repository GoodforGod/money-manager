package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Context;
import io.service.money.manager.IAccountManager;
import io.service.money.model.ParseBox;
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

    private ITransferStorage transferStorage;
    private IAccountManager accountManager;

    @Inject
    public TransferController(ITransferStorage transferStorage, IAccountManager accountManager) {
        this.transferStorage = transferStorage;
        this.accountManager = accountManager;
    }

    public String getTransfer(Context context) {
        final ParseBox parseBox = getPathParam("id", context);
        if (parseBox.isEmpty())
            return convert(parseBox.getRestResponse());

        final Optional<Transfer> transfer = transferStorage.find(parseBox.getParam());
        return (!transfer.isPresent())
                ? errorResponse("Transfer is not presented")
                : validResponse(transfer.get());
    }

    public CompletableFuture<String> computeTransfer(Context context) {
        final ParseBox parseBoxAmount = getQueryParam("amount", context);
        final ParseBox parseBoxFromID = getQueryParam("fromAccountID", context);
        final ParseBox parseBoxToID = getQueryParam("toAccountID", context);
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
            final Optional<Transfer> transferCompleted = accountManager.transfer(transfer);
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
