package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.RecurringExpense;
import com.moneytrackingapp.service.RecurringExpenseService;
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
@RequestMapping("/api/recurring")
@CrossOrigin(origins = "*")
@Tag(name = "Recurring Expenses", description = "Manage default/recurring expense templates")
public class RecurringExpenseController {
    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(RecurringExpenseService recurringExpenseService) {
        this.recurringExpenseService = recurringExpenseService;
    }

    @GetMapping
    @Operation(summary = "List all recurring expenses")
    @ApiResponse(responseCode = "200", description = "Recurring expenses returned successfully")
    public List<RecurringExpense> getAllRecurringExpenses() {
        return recurringExpenseService.listAllRecurringExpenses();
    }

    @PostMapping
    @Operation(summary = "Create a recurring expense")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recurring expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid recurring expense payload")
    })
    public ResponseEntity<RecurringExpense> createRecurringExpense(@Valid @RequestBody RecurringExpense recurringExpense) {
        return ResponseEntity.ok(recurringExpenseService.createRecurringExpense(recurringExpense));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a recurring expense")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recurring expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload or recurring expense not found")
    })
    public ResponseEntity<RecurringExpense> updateRecurringExpense(
            @Parameter(description = "Recurring expense ID") @PathVariable Long id,
            @Valid @RequestBody RecurringExpense recurringExpense) {
        return ResponseEntity.ok(recurringExpenseService.updateRecurringExpense(id, recurringExpense));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recurring expense")
    @ApiResponse(responseCode = "204", description = "Recurring expense deleted successfully")
    public ResponseEntity<Void> deleteRecurringExpense(
            @Parameter(description = "Recurring expense ID") @PathVariable Long id) {
        recurringExpenseService.deleteRecurringExpense(id);
        return ResponseEntity.noContent().build();
    }
}
