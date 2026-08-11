package com.moneytrackingapp.service;

import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.repository.SpendEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendEntryServiceImpl implements SpendEntryService {
    private final SpendEntryRepository repository;

    public SpendEntryServiceImpl(SpendEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SpendEntry> listAllEntries() {
        return repository.findAll();
    }

    @Override
    public SpendEntry createEntry(SpendEntry spendEntry) {
        return repository.save(spendEntry);
    }

    @Override
    public SpendEntry updateEntry(Long id, SpendEntry spendEntry) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setDescription(spendEntry.getDescription());
                    existing.setCategory(spendEntry.getCategory());
                    existing.setAmount(spendEntry.getAmount());
                    existing.setDate(spendEntry.getDate());
                    existing.setNote(spendEntry.getNote());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Entry not found: " + id));
    }

    @Override
    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllEntries() {
        repository.deleteAll();
    }
}
