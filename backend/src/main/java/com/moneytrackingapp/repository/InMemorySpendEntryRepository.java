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
    public List<SpendEntry> findAllByUserId(Long userId) {
        return storage.values().stream()
                .filter(entry -> userId.equals(entry.getUserId()))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<SpendEntry> findByIdAndUserId(Long id, Long userId) {
        return Optional.ofNullable(storage.get(id))
                .filter(entry -> userId.equals(entry.getUserId()));
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
    public void deleteByIdAndUserId(Long id, Long userId) {
        findByIdAndUserId(id, userId).ifPresent(entry -> storage.remove(entry.getId()));
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        storage.values().removeIf(entry -> userId.equals(entry.getUserId()));
    }
}
