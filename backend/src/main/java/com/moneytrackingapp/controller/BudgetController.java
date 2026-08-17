package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
@Tag(name = "Budgets", description = "Manage category budgets by monthly or weekly period")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    @Operation(summary = "List all budgets")
    @ApiResponse(responseCode = "200", description = "Budgets returned successfully")
    public List<Budget> getAllBudgets() {
        return budgetService.listAllBudgets();
    }

    @PostMapping
    @Operation(summary = "Create a budget")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid budget payload")
    })
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a budget")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid budget payload or budget not found")
    })
    public ResponseEntity<Budget> updateBudget(
            @Parameter(description = "Budget ID") @PathVariable Long id,
            @Valid @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budget));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a budget")
    @ApiResponse(responseCode = "204", description = "Budget deleted successfully")
    public ResponseEntity<Void> deleteBudget(
            @Parameter(description = "Budget ID") @PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
