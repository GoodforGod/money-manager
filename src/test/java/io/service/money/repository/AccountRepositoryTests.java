package io.service.money.repository;

import io.service.money.model.dao.Account;
import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.AccountRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class AccountRepositoryTests extends Assert {

    private AccountRepository repository = new AccountRepository();

    @After
    public void reset() {
        repository.deleteAll();
    }

    @Test
    public void saveNonExist() {
        Account account = new Account();
        Account save = repository.save(account.getId(), account);
        assertNotNull(save);
        assertEquals(account.getId(), save.getId());
        assertEquals(account.getBalance(), save.getBalance());
    }

    @Test
    public void saveExist() {
        Account account1 = new Account(100);
        Account save1 = repository.save(account1.getId(), account1);
        assertNotNull(save1);

        Optional<Transfer> transfer = save1.transfer(50, "55");
        assertTrue(transfer.isPresent());

        Account reSaved = repository.save(save1.getId(), save1);
        assertNotNull(reSaved);
        assertEquals(save1.getId(), reSaved.getId());
        assertEquals(BigInteger.valueOf(50), reSaved.getBalance());
    }

    @Test
    public void findExist() {
        Account account = new Account();
        Account save = repository.save(account.getId(), account);
        assertNotNull(save);

        Account found = repository.find(account.getId());
        assertNotNull(found);
        assertEquals(save.getId(), found.getId());
        assertEquals(save.getBalance(), found.getBalance());
    }

    @Test
    public void findNonExist() {
        Account account = new Account();
        Account found = repository.find(account.getId());
        assertNull(found);
    }

    @Test
    public void findAllExist() {
        Account account1 = new Account();
        Account account2 = new Account();
        Account save1 = repository.save(account1.getId(), account1);
        Account save2 = repository.save(account2.getId(), account2);
        assertNotNull(save1);
        assertNotNull(save2);

        List<Account> all = repository.findAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    public void findAllNonExist() {
        List<Account> all = repository.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void deleteExist() {
        Account account = new Account();
        Account save = repository.save(account.getId(), account);
        assertNotNull(save);

        repository.deleteById(account.getId());
        Account found = repository.find(save.getId());
        assertNull(found);
    }

    @Test
    public void deleteAllExist() {
        Account account1 = new Account();
        Account account2 = new Account();
        Account save1 = repository.save(account1.getId(), account1);
        Account save2 = repository.save(account2.getId(), account2);
        assertNotNull(save1);
        assertNotNull(save2);

        repository.deleteAll();
        List<Account> all = repository.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
