package io.service.money.storage;

import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.AccountRepository;
import io.service.money.storage.impl.AccountStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class AccountStorageTests extends Assert {

    private AccountStorage storage = new AccountStorage(new AccountRepository());

    @After
    public void reset() {
        storage.deleteAll();
    }

    @Test
    public void saveNonExist() {
        Account account = new Account();
        Optional<Account> save = storage.save(account);
        assertNotNull(save);
        assertTrue(save.isPresent());
        assertEquals(account.getId(), save.get().getId());
        assertEquals(account.getBalance(), save.get().getBalance());
    }

    @Test
    public void saveNonExistList() {
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accounts = Arrays.asList(account1, account2);

        List<Account> saved = storage.save(accounts);
        assertNotNull(saved);
        assertFalse(saved.isEmpty());

        Account saved1 = saved.get(0);
        assertEquals(account1.getId(), saved1.getId());
        assertEquals(account1.getBalance(), saved1.getBalance());

        Account saved2 = saved.get(1);
        assertEquals(account2.getId(), saved2.getId());
        assertEquals(account2.getBalance(), saved2.getBalance());
    }

    @Test
    public void saveExist() {
        Account account = new Account(100);
        Optional<Account> save = storage.save(account);
        assertNotNull(save);
        assertTrue(save.isPresent());

        Optional<Transfer> transfer = save.get().transfer(50, "55");
        assertTrue(transfer.isPresent());

        Optional<Account> reSaved = storage.save(save.get());
        assertNotNull(reSaved);
        assertTrue(reSaved.isPresent());
        assertEquals(save.get().getId(), reSaved.get().getId());
        assertEquals(BigInteger.valueOf(50), reSaved.get().getBalance());
    }

    @Test
    public void findExist() {
        Account account = new Account();
        Optional<Account> save = storage.save(account);
        assertNotNull(save);
        assertTrue(save.isPresent());

        Optional<Account> found = storage.find(account.getId());
        assertNotNull(found);
        assertTrue(found.isPresent());
        assertEquals(save.get().getId(), found.get().getId());
        assertEquals(save.get().getBalance(), found.get().getBalance());
    }

    @Test
    public void findNonExist() {
        Account account = new Account();
        Optional<Account> found = storage.find(account.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void findAllExist() {
        Account account1 = new Account();
        Account account2 = new Account();
        Optional<Account> save1 = storage.save(account1);
        Optional<Account> save2 = storage.save(account2);
        assertNotNull(save1);
        assertTrue(save1.isPresent());
        assertNotNull(save2);
        assertTrue(save2.isPresent());

        List<Account> all = storage.findAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    public void findAllNonExist() {
        List<Account> all = storage.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void deleteExistById() {
        Account account = new Account();
        Optional<Account> save = storage.save(account);
        assertNotNull(save);
        assertTrue(save.isPresent());

        storage.deleteById(account.getId());
        Optional<Account> found = storage.find(account.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void deleteExist() {
        Account account = new Account();
        Optional<Account> save = storage.save(account);
        assertNotNull(save);
        assertTrue(save.isPresent());

        Optional<Account> delete = storage.delete(account);
        assertTrue(delete.isPresent());
        Optional<Account> found = storage.find(account.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void deleteAllExist() {
        Account account1 = new Account();
        Account account2 = new Account();
        Optional<Account> save1 = storage.save(account1);
        Optional<Account> save2 = storage.save(account2);
        assertNotNull(save1);
        assertTrue(save1.isPresent());
        assertNotNull(save2);
        assertTrue(save2.isPresent());

        storage.deleteAll();
        List<Account> all = storage.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
