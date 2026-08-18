package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.RecurringExpense;
import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository {
    List<RecurringExpense> findAllByUserId(Long userId);
    Optional<RecurringExpense> findByIdAndUserId(Long id, Long userId);
    RecurringExpense save(RecurringExpense recurringExpense);
    void deleteByIdAndUserId(Long id, Long userId);
}
