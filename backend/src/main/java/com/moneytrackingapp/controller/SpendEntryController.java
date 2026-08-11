package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.service.SpendEntryService;
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
@RequestMapping("/api/entries")
@CrossOrigin(origins = "*")
@Tag(name = "Spend Entries", description = "Create, list, update, and delete spend entries")
public class SpendEntryController {
    private final SpendEntryService spendEntryService;

    public SpendEntryController(SpendEntryService spendEntryService) {
        this.spendEntryService = spendEntryService;
    }

    @GetMapping
    @Operation(summary = "List all spend entries")
    @ApiResponse(responseCode = "200", description = "Entries returned successfully")
    public List<SpendEntry> getAllEntries() {
        return spendEntryService.listAllEntries();
    }

    @PostMapping
    @Operation(summary = "Create a spend entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid entry payload")
    })
    public ResponseEntity<SpendEntry> createEntry(@Valid @RequestBody SpendEntry spendEntry) {
        return ResponseEntity.ok(spendEntryService.createEntry(spendEntry));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a spend entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid entry payload or entry not found")
    })
    public ResponseEntity<SpendEntry> updateEntry(
            @Parameter(description = "Spend entry ID") @PathVariable Long id,
            @Valid @RequestBody SpendEntry spendEntry) {
        return ResponseEntity.ok(spendEntryService.updateEntry(id, spendEntry));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a spend entry")
    @ApiResponse(responseCode = "204", description = "Entry deleted successfully")
    public ResponseEntity<Void> deleteEntry(
            @Parameter(description = "Spend entry ID") @PathVariable Long id) {
        spendEntryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all spend entries")
    @ApiResponse(responseCode = "204", description = "All entries deleted successfully")
    public ResponseEntity<Void> deleteAllEntries() {
        spendEntryService.deleteAllEntries();
        return ResponseEntity.noContent().build();
    }
}
