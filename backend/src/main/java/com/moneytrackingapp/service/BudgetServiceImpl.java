package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository repository;

    public BudgetServiceImpl(BudgetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Budget> listAllBudgets() {
        return repository.findAll();
    }

    @Override
    public Budget createBudget(Budget budget) {
        return repository.save(budget);
    }

    @Override
    public Budget updateBudget(Long id, Budget budget) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCategory(budget.getCategory());
                    existing.setAmount(budget.getAmount());
                    existing.setPeriod(budget.getPeriod());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Budget not found: " + id));
    }

    @Override
    public void deleteBudget(Long id) {
        repository.deleteById(id);
    }
}
