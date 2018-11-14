package io.service.money.storage;

import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;
import io.service.money.storage.impl.TransferStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class TransferStorageTests extends Assert {

    private TransferStorage storage = new TransferStorage(new TransferRepository());

    @After
    public void reset() {
        storage.deleteAll();
    }

    @Test
    public void saveNonExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Optional<Transfer> saved = storage.save(transfer);
        assertNotNull(saved);
        assertTrue(saved.isPresent());

        assertEquals(transfer.getId(), saved.get().getId());
        assertEquals(transfer.getAmount(), saved.get().getAmount());
        assertEquals(transfer.getFromAccountID(), saved.get().getFromAccountID());
        assertEquals(transfer.getToAccountID(), saved.get().getToAccountID());
    }

    @Test
    public void saveNonExistList() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(500, "3", "4");
        List<Transfer> transfers = Arrays.asList(transfer1, transfer2);
        List<Transfer> saved = storage.save(transfers);
        assertNotNull(saved);
        assertFalse(saved.isEmpty());

        Transfer saved1 = saved.get(0);
        assertEquals(transfer1.getId(), saved1.getId());
        assertEquals(transfer1.getAmount(), saved1.getAmount());
        assertEquals(transfer1.getFromAccountID(), saved1.getFromAccountID());
        assertEquals(transfer1.getToAccountID(), saved1.getToAccountID());

        Transfer saved2 = saved.get(1);
        assertEquals(transfer2.getId(), saved2.getId());
        assertEquals(transfer2.getAmount(), saved2.getAmount());
        assertEquals(transfer2.getFromAccountID(), saved2.getFromAccountID());
        assertEquals(transfer2.getToAccountID(), saved2.getToAccountID());
    }

    @Test
    public void findExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Optional<Transfer> saved = storage.save(transfer);
        assertNotNull(saved);
        assertTrue(saved.isPresent());

        Optional<Transfer> found = storage.find(transfer.getId());
        assertNotNull(found);
        assertTrue(found.isPresent());
        assertEquals(transfer.getAmount(), saved.get().getAmount());
        assertEquals(transfer.getFromAccountID(), saved.get().getFromAccountID());
        assertEquals(transfer.getToAccountID(), saved.get().getToAccountID());
    }

    @Test
    public void findNonExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Optional<Transfer> found = storage.find(transfer.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void findAllExist() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(500, "3", "4");
        Optional<Transfer> saved1 = storage.save(transfer1);
        Optional<Transfer> saved2 = storage.save(transfer2);
        assertNotNull(saved1);
        assertTrue(saved1.isPresent());
        assertNotNull(saved2);
        assertTrue(saved2.isPresent());

        List<Transfer> all = storage.findAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    public void findAllByRecipient() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(50, "1", "4");
        Transfer transfer3 = new Transfer(500, "3", "4");
        Optional<Transfer> saved1 = storage.save(transfer1);
        Optional<Transfer> saved2 = storage.save(transfer2);
        Optional<Transfer> saved3 = storage.save(transfer3);
        assertNotNull(saved1);
        assertTrue(saved1.isPresent());
        assertNotNull(saved2);
        assertTrue(saved2.isPresent());
        assertNotNull(saved3);
        assertTrue(saved3.isPresent());

        List<Transfer> bySender = storage.findByRecipient("4");
        assertNotNull(bySender);
        assertFalse(bySender.isEmpty());
        assertEquals(2, bySender.size());
    }

    @Test
    public void findAllBySender() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(50, "1", "4");
        Transfer transfer3 = new Transfer(500, "3", "4");
        Optional<Transfer> saved1 = storage.save(transfer1);
        Optional<Transfer> saved2 = storage.save(transfer2);
        Optional<Transfer> saved3 = storage.save(transfer3);
        assertNotNull(saved1);
        assertTrue(saved1.isPresent());
        assertNotNull(saved2);
        assertTrue(saved2.isPresent());
        assertNotNull(saved3);
        assertTrue(saved3.isPresent());

        List<Transfer> bySender = storage.findBySender("1");
        assertNotNull(bySender);
        assertFalse(bySender.isEmpty());
        assertEquals(2, bySender.size());
    }

    @Test
    public void findAllNonExist() {
        List<Transfer> all = storage.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void deleteExistById() {
        Transfer transfer = new Transfer(50, "1", "2");
        Optional<Transfer> saved = storage.save(transfer);
        assertNotNull(saved);
        assertTrue(saved.isPresent());

        storage.deleteById(transfer.getId());
        Optional<Transfer> found = storage.find(transfer.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void deleteExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Optional<Transfer> saved = storage.save(transfer);
        assertNotNull(saved);
        assertTrue(saved.isPresent());

        Optional<Transfer> delete = storage.delete(transfer);
        assertTrue(delete.isPresent());
        Optional<Transfer> found = storage.find(transfer.getId());
        assertNotNull(found);
        assertFalse(found.isPresent());
    }

    @Test
    public void deleteAllExist() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(500, "3", "4");
        Optional<Transfer> saved1 = storage.save(transfer1);
        Optional<Transfer> saved2 = storage.save(transfer2);
        assertNotNull(saved1);
        assertTrue(saved1.isPresent());
        assertNotNull(saved2);
        assertTrue(saved2.isPresent());

        storage.deleteAll();
        List<Transfer> all = storage.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
