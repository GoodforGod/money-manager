package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Context;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Account;
import io.service.money.storage.IAccountStorage;

import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 13.11.2018
 */
@Singleton
public class AccountController extends BasicController {

    private IAccountStorage accountStorage;

    @Inject
    public AccountController(IAccountStorage accountStorage) {
        this.accountStorage = accountStorage;
    }

    public String getAccount(Context context) {
        final ParseBox parseBox = getPathParam("id", context);
        if (parseBox.isEmpty())
            return convert(parseBox.getRestResponse());

        final Optional<Account> account = accountStorage.find(parseBox.getParam());
        return (account.isPresent())
                ? validResponse(account.get())
                : errorResponse("Account does not exist");
    }

    public String createAccount(Context context) {
        final ParseBox parseBox = getPathParam("deposit", context);
        final long deposit = parseLongOrZero(parseBox.getParam());

        final Optional<Account> account = accountStorage.save(new Account(deposit));
        return (account.isPresent())
                ? validResponse(account.get())
                : errorResponse("Can not create account");
    }

    private static long parseLongOrZero(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
