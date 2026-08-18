package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.SpendEntry;
import java.util.List;
import java.util.Optional;

public interface SpendEntryRepository {
    List<SpendEntry> findAllByUserId(Long userId);
    Optional<SpendEntry> findByIdAndUserId(Long id, Long userId);
    SpendEntry save(SpendEntry spendEntry);
    void deleteByIdAndUserId(Long id, Long userId);
    void deleteAllByUserId(Long userId);
}
