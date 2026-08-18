package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.repository.SpendEntryRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendEntryServiceImpl implements SpendEntryService {
    private final SpendEntryRepository repository;
    private final CurrentUser currentUser;

    public SpendEntryServiceImpl(SpendEntryRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    @Override
    public List<SpendEntry> listAllEntries() {
        return repository.findAllByUserId(currentUser.requireUserId());
    }

    @Override
    public SpendEntry createEntry(SpendEntry spendEntry) {
        spendEntry.setUserId(currentUser.requireUserId());
        return repository.save(spendEntry);
    }

    @Override
    public SpendEntry updateEntry(Long id, SpendEntry spendEntry) {
        Long userId = currentUser.requireUserId();
        return repository.findByIdAndUserId(id, userId)
                .map(existing -> {
                    existing.setDescription(spendEntry.getDescription());
                    existing.setCategory(spendEntry.getCategory());
                    existing.setAmount(spendEntry.getAmount());
                    existing.setDate(spendEntry.getDate());
                    existing.setNote(spendEntry.getNote());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found: " + id));
    }

    @Override
    public void deleteEntry(Long id) {
        Long userId = currentUser.requireUserId();
        SpendEntry existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found: " + id));
        repository.deleteByIdAndUserId(existing.getId(), userId);
    }

    @Override
    public void deleteAllEntries() {
        repository.deleteAllByUserId(currentUser.requireUserId());
    }
}
