package io.service.money.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.service.money.config.JavalinInjector;
import io.service.money.model.dao.Account;
import io.service.money.model.dto.RestResponse;
import io.service.money.support.HttpExecutor;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class AccountControllerTests extends JavalinInjector {

    public static Account createAccount(int deposit, HttpExecutor executor, Gson gson) throws IOException {
        Type type = new TypeToken<RestResponse<Account>>() {}.getType();

        String json = executor.put(SERVER_HOST + "/account/create/" + deposit);
        RestResponse<Account> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertNotNull(response.getResult().getBalance());
        assertEquals(deposit, response.getResult().getBalance().longValue());

        return response.getResult();
    }

    @Test
    public void getAllAccountsExist() throws IOException {
        Account account1 = createAccount(100, executor, gson);
        Account account2 = createAccount(100, executor, gson);

        Type type = new TypeToken<RestResponse<List<Account>>>() {}.getType();

        String json = executor.get(SERVER_HOST + "/account/all");
        RestResponse<List<Account>> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertNotNull(response.getResult());
        assertFalse(response.getResult().isEmpty());

        boolean haveAccount1 = response.getResult().stream().anyMatch(a -> a.equals(account1));
        boolean haveAccount2 = response.getResult().stream().anyMatch(a -> a.equals(account2));
        assertTrue(haveAccount1);
        assertTrue(haveAccount2);
    }

    @Test
    public void getAccountNonExist() throws IOException {
        String json = executor.get(SERVER_HOST + "/account/1");
        RestResponse response = gson.fromJson(json, RestResponse.class);
        assertNotNull(response);
        assertTrue(response.isError());
        assertNotNull(response.getErrorDetails());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void createAccountWithDepositValid() throws IOException {
        createAccount(100, executor, gson);
    }

    @Test
    public void getAccountExist() throws IOException {
        Type type = new TypeToken<RestResponse<Account>>() {}.getType();

        String json = executor.put(SERVER_HOST + "/account/create/100");
        RestResponse<Account> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertNotNull(response.getResult().getBalance());
        assertEquals(100, response.getResult().getBalance().longValue());

        String jsonFound = executor.get(SERVER_HOST + "/account/" + response.getResult().getId());
        RestResponse<Account> responseFound = gson.fromJson(jsonFound, type);
        assertNotNull(responseFound);
        assertFalse(responseFound.isError());
        assertTrue(responseFound.getErrorDetails().isEmpty());
        assertNotNull(responseFound.getResult());
        assertEquals(response.getResult().getId(), responseFound.getResult().getId());
        assertEquals(response.getResult().getBalance(), responseFound.getResult().getBalance());
    }

    @Test
    public void createAccountWithDepositInvalid() throws IOException {
        Type type = new TypeToken<RestResponse<Account>>() {}.getType();

        String json = executor.put(SERVER_HOST + "/account/create/-500");
        RestResponse<Account> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertNotNull(response.getResult().getBalance());
        assertEquals(0, response.getResult().getBalance().longValue());
    }
}
