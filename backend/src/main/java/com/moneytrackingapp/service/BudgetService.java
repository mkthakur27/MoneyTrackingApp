package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Budget;
import java.util.List;

public interface BudgetService {
    List<Budget> listAllBudgets();
    Budget createBudget(Budget budget);
    Budget updateBudget(Long id, Budget budget);
    void deleteBudget(Long id);
}
