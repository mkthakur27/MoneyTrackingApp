package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.service.SpendEntryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin(origins = "*")
public class SpendEntryController {
    private final SpendEntryService spendEntryService;

    public SpendEntryController(SpendEntryService spendEntryService) {
        this.spendEntryService = spendEntryService;
    }

    @GetMapping
    public List<SpendEntry> getAllEntries() {
        return spendEntryService.listAllEntries();
    }

    @PostMapping
    public ResponseEntity<SpendEntry> createEntry(@Valid @RequestBody SpendEntry spendEntry) {
        return ResponseEntity.ok(spendEntryService.createEntry(spendEntry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpendEntry> updateEntry(@PathVariable Long id, @Valid @RequestBody SpendEntry spendEntry) {
        return ResponseEntity.ok(spendEntryService.updateEntry(id, spendEntry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        spendEntryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllEntries() {
        spendEntryService.deleteAllEntries();
        return ResponseEntity.noContent().build();
    }
}
