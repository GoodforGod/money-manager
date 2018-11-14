package io.service.money.model.dto;

import io.service.money.model.dao.Account;

import java.math.BigInteger;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
public class AccountTO {

    private String id;
    private BigInteger balance;

    private AccountTO(String id, BigInteger balance) {
        this.id = id;
        this.balance = balance;
    }

    public static AccountTO of(Account account) {
        return (account == null)
                ? null
                : new AccountTO(account.getId(), account.getBalance());
    }

    public String getId() {
        return id;
    }

    public BigInteger getBalance() {
        return balance;
    }
}
