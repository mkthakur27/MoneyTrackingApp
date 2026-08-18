package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> {
    List<RecurringExpense> findAllByUserIdOrderByIdAsc(Long userId);
    Optional<RecurringExpense> findByIdAndUserId(Long id, Long userId);
}
