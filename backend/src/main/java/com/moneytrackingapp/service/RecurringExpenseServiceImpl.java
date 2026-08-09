package com.moneytrackingapp.service;

import com.moneytrackingapp.model.RecurringExpense;
import com.moneytrackingapp.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringExpenseServiceImpl implements RecurringExpenseService {
    private final RecurringExpenseRepository repository;

    public RecurringExpenseServiceImpl(RecurringExpenseRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RecurringExpense> listAllRecurringExpenses() {
        return repository.findAll();
    }

    @Override
    public RecurringExpense createRecurringExpense(RecurringExpense recurringExpense) {
        return repository.save(recurringExpense);
    }

    @Override
    public RecurringExpense updateRecurringExpense(Long id, RecurringExpense recurringExpense) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setDescription(recurringExpense.getDescription());
                    existing.setCategory(recurringExpense.getCategory());
                    existing.setAmount(recurringExpense.getAmount());
                    existing.setPeriod(recurringExpense.getPeriod());
                    existing.setNote(recurringExpense.getNote());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Recurring expense not found: " + id));
    }

    @Override
    public void deleteRecurringExpense(Long id) {
        repository.deleteById(id);
    }
}
