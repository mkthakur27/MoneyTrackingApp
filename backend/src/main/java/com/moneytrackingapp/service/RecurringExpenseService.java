package com.moneytrackingapp.service;

import com.moneytrackingapp.model.RecurringExpense;
import java.util.List;

public interface RecurringExpenseService {
    List<RecurringExpense> listAllRecurringExpenses();
    RecurringExpense createRecurringExpense(RecurringExpense recurringExpense);
    RecurringExpense updateRecurringExpense(Long id, RecurringExpense recurringExpense);
    void deleteRecurringExpense(Long id);
}
