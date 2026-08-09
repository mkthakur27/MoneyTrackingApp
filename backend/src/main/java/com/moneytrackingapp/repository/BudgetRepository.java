package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository {
    List<Budget> findAll();
    Optional<Budget> findById(Long id);
    Budget save(Budget budget);
    void deleteById(Long id);
}
