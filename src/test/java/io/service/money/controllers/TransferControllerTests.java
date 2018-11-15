package io.service.money.controllers;

import com.google.gson.reflect.TypeToken;
import io.service.money.config.JavalinInjector;
import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.model.dto.RestResponse;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class TransferControllerTests extends JavalinInjector {

    @Test
    public void getTransferAllExist() throws IOException {
        performValidTransfer();
        performValidTransfer();

        Type type = new TypeToken<RestResponse<List<Transfer>>>() {}.getType();

        String json = executor.get(SERVER_HOST + "/transfer/all");
        RestResponse<List<Transfer>> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertNotNull(response.getResult());
        assertFalse(response.getResult().isEmpty());
    }

    @Test
    public void getTransferNonExist() throws IOException {
        String json = executor.get(SERVER_HOST + "/transfer/1");
        RestResponse response = gson.fromJson(json, RestResponse.class);
        assertNotNull(response);
        assertTrue(response.isError());
        assertNotNull(response.getErrorDetails());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void performValidTransfer() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account1 = AccountControllerTests.createAccount(200, executor, gson);
        Account account2 = AccountControllerTests.createAccount(550, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=50&senderID=" + account1.getId() + "&receiverID=" + account2.getId());
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertEquals(account2.getId(), response.getResult().getReceiverID());
        assertEquals(account1.getId(), response.getResult().getSenderID());
        assertEquals(50, response.getResult().getAmount());

        Optional<Account> accountFrom = accountStorage.find(account1.getId());
        assertNotNull(accountFrom);
        assertTrue(accountFrom.isPresent());
        assertEquals(150, accountFrom.get().getBalance().longValue());

        Optional<Account> accountTo = accountStorage.find(account2.getId());
        assertNotNull(accountTo);
        assertTrue(accountTo.isPresent());
        assertEquals(600, accountTo.get().getBalance().longValue());
    }

    @Test
    public void performTransferFromAccountNotExist() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account1 = AccountControllerTests.createAccount(200, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=50&senderID=" + account1.getId() + "&receiverID=1");
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertTrue(response.isError());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void performTransferToAccountNotExist() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account2 = AccountControllerTests.createAccount(550, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=50&senderID=1" + "&receiverID=" + account2.getId());
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertTrue(response.isError());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void performTransferNotEnoughAmount() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account1 = AccountControllerTests.createAccount(200, executor, gson);
        Account account2 = AccountControllerTests.createAccount(550, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=500000&senderID=" + account1.getId() + "&receiverID=" + account2.getId());
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertTrue(response.isError());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void performTransferInvalidAmount() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account1 = AccountControllerTests.createAccount(200, executor, gson);
        Account account2 = AccountControllerTests.createAccount(550, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=-1000&senderID=" + account1.getId() + "&receiverID=" + account2.getId());
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertTrue(response.isError());
        assertFalse(response.getErrorDetails().isEmpty());
    }

    @Test
    public void getTransferExist() throws IOException {
        Type type = new TypeToken<RestResponse<Transfer>>() {}.getType();

        Account account1 = AccountControllerTests.createAccount(200, executor, gson);
        Account account2 = AccountControllerTests.createAccount(550, executor, gson);

        String json = executor.put(SERVER_HOST + "/transfer" + "?amount=50&senderID=" + account1.getId() + "&receiverID=" + account2.getId());
        RestResponse<Transfer> response = gson.fromJson(json, type);
        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.getErrorDetails().isEmpty());
        assertEquals(account2.getId(), response.getResult().getReceiverID());
        assertEquals(account1.getId(), response.getResult().getSenderID());
        assertEquals(50, response.getResult().getAmount());

        Optional<Account> accountFrom = accountStorage.find(account1.getId());
        assertNotNull(accountFrom);
        assertTrue(accountFrom.isPresent());
        assertEquals(150, accountFrom.get().getBalance().longValue());

        Optional<Account> accountTo = accountStorage.find(account2.getId());
        assertNotNull(accountTo);
        assertTrue(accountTo.isPresent());
        assertEquals(600, accountTo.get().getBalance().longValue());

        Optional<Transfer> transfer = transferStorage.find(response.getResult().getId());
        assertNotNull(transfer);
        assertTrue(transfer.isPresent());
        assertEquals(response.getResult().getId(), transfer.get().getId());
        assertEquals(response.getResult().getReceiverID(), transfer.get().getReceiverID());
        assertEquals(response.getResult().getSenderID(), transfer.get().getSenderID());
        assertEquals(response.getResult().getAmount(), transfer.get().getAmount());
    }
}
