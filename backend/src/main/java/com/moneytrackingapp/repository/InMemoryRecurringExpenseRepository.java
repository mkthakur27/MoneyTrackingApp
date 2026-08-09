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
    public List<RecurringExpense> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<RecurringExpense> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
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
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
