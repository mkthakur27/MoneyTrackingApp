package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.repository.SpendEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpendEntryServiceImpl implements SpendEntryService {
    private final SpendEntryRepository repository;

    public SpendEntryServiceImpl(SpendEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpendEntry> listAllEntries() {
        return repository.findAllByOrderByDateDescIdDesc();
    }

    @Override
    @Transactional
    public SpendEntry createEntry(SpendEntry spendEntry) {
        return repository.save(spendEntry);
    }

    @Override
    @Transactional
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
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found: " + id));
    }

    @Override
    @Transactional
    public void deleteEntry(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Entry not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllEntries() {
        repository.deleteAll();
    }
}
