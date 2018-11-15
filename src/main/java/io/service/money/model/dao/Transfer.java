package io.service.money.model.dao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Why its Transfer instead of Transaction?
 * Some things just, happens.
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class Transfer extends BaseModel<String> {

    private final LocalDateTime timestamp;

    private final long amount;
    private final String senderID;
    private final String receiverID;

    public Transfer(long amount, String senderID, String receiverID) {
        super(UUID.randomUUID().toString());
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }
}
