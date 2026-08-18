package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.RecurringExpense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRecurringExpenseRepository implements RecurringExpenseRepository {
    private final ConcurrentHashMap<Long, RecurringExpense> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<RecurringExpense> findAllByUserId(Long userId) {
        return storage.values().stream()
                .filter(item -> userId.equals(item.getUserId()))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<RecurringExpense> findByIdAndUserId(Long id, Long userId) {
        return Optional.ofNullable(storage.get(id))
                .filter(item -> userId.equals(item.getUserId()));
    }

    @Override
    public RecurringExpense save(RecurringExpense recurringExpense) {
        if (recurringExpense.getId() == null) {
            recurringExpense.setId(idGenerator.getAndIncrement());
        }
        storage.put(recurringExpense.getId(), recurringExpense);
        return recurringExpense;
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        findByIdAndUserId(id, userId).ifPresent(item -> storage.remove(item.getId()));
    }
}
