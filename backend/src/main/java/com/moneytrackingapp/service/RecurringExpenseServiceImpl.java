package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.RecurringExpense;
import com.moneytrackingapp.repository.RecurringExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecurringExpenseServiceImpl implements RecurringExpenseService {
    private final RecurringExpenseRepository repository;

    public RecurringExpenseServiceImpl(RecurringExpenseRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecurringExpense> listAllRecurringExpenses() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional
    public RecurringExpense createRecurringExpense(RecurringExpense recurringExpense) {
        return repository.save(recurringExpense);
    }

    @Override
    @Transactional
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
                .orElseThrow(() -> new ResourceNotFoundException("Recurring expense not found: " + id));
    }

    @Override
    @Transactional
    public void deleteRecurringExpense(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurring expense not found: " + id);
        }
        repository.deleteById(id);
    }
}
