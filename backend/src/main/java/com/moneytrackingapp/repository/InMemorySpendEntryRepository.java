package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.SpendEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemorySpendEntryRepository implements SpendEntryRepository {
    private final ConcurrentHashMap<Long, SpendEntry> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<SpendEntry> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<SpendEntry> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public SpendEntry save(SpendEntry spendEntry) {
        if (spendEntry.getId() == null) {
            spendEntry.setId(idGenerator.getAndIncrement());
        }
        storage.put(spendEntry.getId(), spendEntry);
        return spendEntry;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}
