package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.listAllBudgets();
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @Valid @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budget));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
