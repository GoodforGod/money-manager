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
    private String senderID;
    private String receiverID;

    private TransferTO(LocalDateTime timestamp, long amount, String senderID, String receiverID) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public static TransferTO of(Transfer t) {
        return (t == null)
                ? null
                : new TransferTO(t.getTimestamp(), t.getAmount(), t.getSenderID(), t.getReceiverID());
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
