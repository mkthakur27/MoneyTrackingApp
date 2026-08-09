package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.SpendEntry;
import java.util.List;
import java.util.Optional;

public interface SpendEntryRepository {
    List<SpendEntry> findAll();
    Optional<SpendEntry> findById(Long id);
    SpendEntry save(SpendEntry spendEntry);
    void deleteById(Long id);
    void deleteAll();
}
