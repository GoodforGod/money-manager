package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Context;
import io.service.money.error.TransferException;
import io.service.money.manager.ITransferManager;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Transfer;
import io.service.money.storage.ITransferStorage;

import java.util.List;
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
    private ITransferManager transferManager;

    @Inject
    public TransferController(ITransferStorage transferStorage, ITransferManager transferManager) {
        this.transferStorage = transferStorage;
        this.transferManager = transferManager;
    }

    public String getAllTransfers(Context context) {
        final List<Transfer> all = transferStorage.findAll();
        return (all.isEmpty())
                ? errorResponse("No records found")
                : validResponse(all);
    }

    /**
     * Url Parameters for GET endpoint
     * "id" - transfer id
     *
     * @see io.service.money.model.dto.RestResponse
     * @return restResponse with transfer
     */
    public String getTransfer(Context context) {
        final ParseBox parseBox = getPathParam("id", context);
        if (parseBox.isEmpty())
            return convert(parseBox.getRestResponse());

        final Optional<Transfer> transfer = transferStorage.find(parseBox.getParam());
        return (!transfer.isPresent())
                ? errorResponse("Transfer is not presented")
                : validResponse(transfer.get());
    }

    /**
     * Url Parameters for PUT endpoint
     * "amount" - amount to send
     * "senderID" - sender account
     * "receiverID" - receiver account
     *
     * Example: /transfer?amount=10&senderID=1&receiverID=2
     *
     * @see io.service.money.model.dto.RestResponse
     * @return restResponse with transfer
     */
    public CompletableFuture<String> computeTransfer(Context context) {
        final ParseBox parseBoxAmount = getQueryParam("amount", context);
        final ParseBox senderBox = getQueryParam("senderID", context);
        final ParseBox receiverBox = getQueryParam("receiverID", context);
        final ParseBox errorParseBox = Stream.of(parseBoxAmount, senderBox, receiverBox)
                .filter(ParseBox::isEmpty)
                .findFirst()
                .orElse(null);

        if (errorParseBox != null)
            return CompletableFuture.completedFuture(convert(errorParseBox.getRestResponse()));

        final long amount = parseLongOrZero(parseBoxAmount.getParam());
        if (amount < 1)
            return CompletableFuture.completedFuture(errorResponse("Incorrect transfer amount"));

        return CompletableFuture.supplyAsync(() -> {
            try {
                final Transfer transfer = new Transfer(amount, senderBox.getParam(), receiverBox.getParam());
                return validResponse(transferManager.transfer(transfer));
            } catch (TransferException e) {
                return errorResponse(e.getMessage());
            }
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
