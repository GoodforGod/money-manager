package io.service.money.model.dao;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class Account extends BaseModel<String> {

    private BigInteger balance;

    public Account() {
        this(BigInteger.valueOf(0));
    }

    public Account(long initialBalance) {
        this(BigInteger.valueOf(initialBalance));
    }

    public Account(BigInteger initialBalance) {
        super(UUID.randomUUID().toString());

        this.balance = (initialBalance.compareTo(BigInteger.valueOf(0)) > 0)
                ? initialBalance
                : BigInteger.valueOf(0);
    }

    public BigInteger getBalance() {
        return balance;
    }

    public BigInteger accept(Transfer transfer) {
        return balance.add(BigInteger.valueOf(transfer.getAmount()));
    }

    public Optional<Transfer> transfer(long value, String toAccountID) {
        if (balance.longValue() < value)
            return Optional.empty();

        this.balance = balance.subtract(BigInteger.valueOf(value));
        final Transfer transfer = new Transfer(value, this.id, toAccountID);
        return Optional.of(transfer);
    }
}
