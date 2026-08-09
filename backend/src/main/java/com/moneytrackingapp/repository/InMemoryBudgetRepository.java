package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Budget;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBudgetRepository implements BudgetRepository {
    private final ConcurrentHashMap<Long, Budget> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Budget> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Budget save(Budget budget) {
        if (budget.getId() == null) {
            budget.setId(idGenerator.getAndIncrement());
        }
        storage.put(budget.getId(), budget);
        return budget;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
