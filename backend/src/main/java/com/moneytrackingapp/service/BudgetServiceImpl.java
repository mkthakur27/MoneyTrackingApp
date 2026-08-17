package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository repository;

    public BudgetServiceImpl(BudgetRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> listAllBudgets() {
        return repository.findAllByOrderByIdAsc();
    }

    @Override
    @Transactional
    public Budget createBudget(Budget budget) {
        return repository.save(budget);
    }

    @Override
    @Transactional
    public Budget updateBudget(Long id, Budget budget) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCategory(budget.getCategory());
                    existing.setAmount(budget.getAmount());
                    existing.setPeriod(budget.getPeriod());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
    }

    @Override
    @Transactional
    public void deleteBudget(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Budget not found: " + id);
        }
        repository.deleteById(id);
    }
}
