package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.RecurringExpense;
import com.moneytrackingapp.service.RecurringExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@CrossOrigin(origins = "*")
public class RecurringExpenseController {
    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(RecurringExpenseService recurringExpenseService) {
        this.recurringExpenseService = recurringExpenseService;
    }

    @GetMapping
    public List<RecurringExpense> getAllRecurringExpenses() {
        return recurringExpenseService.listAllRecurringExpenses();
    }

    @PostMapping
    public ResponseEntity<RecurringExpense> createRecurringExpense(@Valid @RequestBody RecurringExpense recurringExpense) {
        return ResponseEntity.ok(recurringExpenseService.createRecurringExpense(recurringExpense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringExpense> updateRecurringExpense(@PathVariable Long id, @Valid @RequestBody RecurringExpense recurringExpense) {
        return ResponseEntity.ok(recurringExpenseService.updateRecurringExpense(id, recurringExpense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringExpense(@PathVariable Long id) {
        recurringExpenseService.deleteRecurringExpense(id);
        return ResponseEntity.noContent().build();
    }
}
