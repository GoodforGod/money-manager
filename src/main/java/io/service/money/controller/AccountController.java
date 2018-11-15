package io.service.money.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Context;
import io.service.money.model.ParseBox;
import io.service.money.model.dao.Account;
import io.service.money.storage.IAccountStorage;

import java.util.List;
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

    public String getAllAccounts(Context context) {
        final List<Account> all = accountStorage.findAll();
        return (all.isEmpty())
                ? errorResponse("No records found")
                : validResponse(all);
    }

    /**
     * Url Parameters for GET endpoint
     * "id" - account id
     *
     * @see io.service.money.model.dto.RestResponse
     * @return restResponse with account
     */
    public String getAccount(Context context) {
        final ParseBox parseBox = getPathParam("id", context);
        if (parseBox.isEmpty())
            return convert(parseBox.getRestResponse());

        final Optional<Account> account = accountStorage.find(parseBox.getParam());
        return (account.isPresent())
                ? validResponse(account.get())
                : errorResponse("Account does not exist");
    }

    /**
     * Url Parameters for PUT endpoint
     * "deposit" - initial balance
     *
     * @see io.service.money.model.dto.RestResponse
     * @return restResponse with account
     */
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
