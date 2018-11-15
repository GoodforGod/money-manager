package io.service.money.error;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class TransferException extends RuntimeException {

    public TransferException(String message) {
        super(message);
    }
}
