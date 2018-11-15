package io.service.money.repository;

import io.service.money.model.dao.Transfer;
import io.service.money.repository.impl.TransferRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 14.11.2018
 */
public class TransferRepositoryTests extends Assert {

    private TransferRepository repository = new TransferRepository();

    @After
    public void reset() {
        repository.deleteAll();
    }

    @Test
    public void saveNonExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Transfer saved = repository.save(transfer.getId(), transfer);
        assertNotNull(saved);
        assertEquals(transfer.getId(), saved.getId());
        assertEquals(transfer.getAmount(), saved.getAmount());
        assertEquals(transfer.getSenderID(), saved.getSenderID());
        assertEquals(transfer.getReceiverID(), saved.getReceiverID());
    }

    @Test
    public void findExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Transfer saved = repository.save(transfer.getId(), transfer);
        assertNotNull(saved);

        Transfer found = repository.find(transfer.getId());
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getAmount(), found.getAmount());
        assertEquals(saved.getSenderID(), found.getSenderID());
        assertEquals(saved.getReceiverID(), found.getReceiverID());
    }

    @Test
    public void findNonExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Transfer found = repository.find(transfer.getId());
        assertNull(found);
    }

    @Test
    public void findAllExist() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(500, "3", "4");
        Transfer save1 = repository.save(transfer1.getId(), transfer1);
        Transfer save2 = repository.save(transfer2.getId(), transfer2);
        assertNotNull(save1);
        assertNotNull(save2);

        List<Transfer> all = repository.findAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertEquals(2, all.size());
    }

    @Test
    public void findAllNonExist() {
        List<Transfer> all = repository.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }

    @Test
    public void deleteExist() {
        Transfer transfer = new Transfer(50, "1", "2");
        Transfer save = repository.save(transfer.getId(), transfer);
        assertNotNull(save);

        repository.deleteById(transfer.getId());
        Transfer found = repository.find(save.getId());
        assertNull(found);
    }

    @Test
    public void deleteAllExist() {
        Transfer transfer1 = new Transfer(50, "1", "2");
        Transfer transfer2 = new Transfer(500, "3", "4");
        Transfer save1 = repository.save(transfer1.getId(), transfer1);
        Transfer save2 = repository.save(transfer2.getId(), transfer2);
        assertNotNull(save1);
        assertNotNull(save2);

        repository.deleteAll();
        List<Transfer> all = repository.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
