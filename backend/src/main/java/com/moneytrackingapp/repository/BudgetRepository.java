package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByUserIdOrderByIdAsc(Long userId);
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
}
