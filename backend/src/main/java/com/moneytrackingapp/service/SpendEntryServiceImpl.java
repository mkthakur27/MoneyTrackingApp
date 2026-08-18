package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.repository.SpendEntryRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<SpendEntry> listAllEntries() {
        return repository.findAllByUserIdOrderByDateDescIdDesc(currentUser.requireUserId());
    }

    @Override
    @Transactional
    public SpendEntry createEntry(SpendEntry spendEntry) {
        spendEntry.setUserId(currentUser.requireUserId());
        return repository.save(spendEntry);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteEntry(Long id) {
        Long userId = currentUser.requireUserId();
        SpendEntry existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found: " + id));
        repository.delete(existing);
    }

    @Override
    @Transactional
    public void deleteAllEntries() {
        repository.deleteAllByUserId(currentUser.requireUserId());
    }
}
