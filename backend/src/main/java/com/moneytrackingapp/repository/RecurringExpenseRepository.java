package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.RecurringExpense;
import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository {
    List<RecurringExpense> findAll();
    Optional<RecurringExpense> findById(Long id);
    RecurringExpense save(RecurringExpense recurringExpense);
    void deleteById(Long id);
}
