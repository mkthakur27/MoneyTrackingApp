package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.repository.BudgetRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository repository;
    private final CurrentUser currentUser;

    public BudgetServiceImpl(BudgetRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    @Override
    public List<Budget> listAllBudgets() {
        return repository.findAllByUserId(currentUser.requireUserId());
    }

    @Override
    public Budget createBudget(Budget budget) {
        budget.setUserId(currentUser.requireUserId());
        return repository.save(budget);
    }

    @Override
    public Budget updateBudget(Long id, Budget budget) {
        Long userId = currentUser.requireUserId();
        return repository.findByIdAndUserId(id, userId)
                .map(existing -> {
                    existing.setCategory(budget.getCategory());
                    existing.setAmount(budget.getAmount());
                    existing.setPeriod(budget.getPeriod());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
    }

    @Override
    public void deleteBudget(Long id) {
        Long userId = currentUser.requireUserId();
        Budget existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
        repository.deleteByIdAndUserId(existing.getId(), userId);
    }
}
