package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.RecurringExpense;
import com.moneytrackingapp.repository.RecurringExpenseRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecurringExpenseServiceImpl implements RecurringExpenseService {
    private final RecurringExpenseRepository repository;
    private final CurrentUser currentUser;

    public RecurringExpenseServiceImpl(RecurringExpenseRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecurringExpense> listAllRecurringExpenses() {
        return repository.findAllByUserIdOrderByIdAsc(currentUser.requireUserId());
    }

    @Override
    @Transactional
    public RecurringExpense createRecurringExpense(RecurringExpense recurringExpense) {
        recurringExpense.setUserId(currentUser.requireUserId());
        return repository.save(recurringExpense);
    }

    @Override
    @Transactional
    public RecurringExpense updateRecurringExpense(Long id, RecurringExpense recurringExpense) {
        Long userId = currentUser.requireUserId();
        return repository.findByIdAndUserId(id, userId)
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
        Long userId = currentUser.requireUserId();
        RecurringExpense existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurring expense not found: " + id));
        repository.delete(existing);
    }
}
