package io.service.money.model.dto;

import io.service.money.model.dao.Transfer;

import java.time.LocalDateTime;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class TransferTO {

    private LocalDateTime timestamp;

    private long amount;
    private String fromAccountID;
    private String toAccountID;

    private TransferTO(LocalDateTime timestamp, long amount, String fromAccountID, String toAccountID) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
    }

    private static TransferTO of(Transfer t) {
        return (t == null)
                ? null
                : new TransferTO(t.getTimestamp(), t.getAmount(), t.getFromAccountID(), t.getToAccountID());
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public String getFromAccountID() {
        return fromAccountID;
    }

    public String getToAccountID() {
        return toAccountID;
    }
}
