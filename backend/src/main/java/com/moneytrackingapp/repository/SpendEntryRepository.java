package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.SpendEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpendEntryRepository extends JpaRepository<SpendEntry, Long> {
    List<SpendEntry> findAllByOrderByDateDescIdDesc();
}
