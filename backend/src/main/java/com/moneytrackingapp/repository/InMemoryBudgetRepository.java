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
    public List<Budget> findAllByUserId(Long userId) {
        return storage.values().stream()
                .filter(budget -> userId.equals(budget.getUserId()))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<Budget> findByIdAndUserId(Long id, Long userId) {
        return Optional.ofNullable(storage.get(id))
                .filter(budget -> userId.equals(budget.getUserId()));
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
    public void deleteByIdAndUserId(Long id, Long userId) {
        findByIdAndUserId(id, userId).ifPresent(budget -> storage.remove(budget.getId()));
    }
}
