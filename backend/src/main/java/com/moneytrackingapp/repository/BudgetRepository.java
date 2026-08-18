package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository {
    List<Budget> findAllByUserId(Long userId);
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    Budget save(Budget budget);
    void deleteByIdAndUserId(Long id, Long userId);
}
