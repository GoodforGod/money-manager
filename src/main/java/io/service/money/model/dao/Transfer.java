package io.service.money.model.dao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class Transfer extends BaseModel<String> {

    private final LocalDateTime timestamp;

    private final long amount;
    private final String fromAccountID;
    private final String toAccountID;

    public Transfer(long amount, String fromAccountID, String toAccountID) {
        super(UUID.randomUUID().toString());
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
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
