package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.SpendEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpendEntryRepository extends JpaRepository<SpendEntry, Long> {
    List<SpendEntry> findAllByUserIdOrderByDateDescIdDesc(Long userId);
    Optional<SpendEntry> findByIdAndUserId(Long id, Long userId);
    void deleteAllByUserId(Long userId);
}
