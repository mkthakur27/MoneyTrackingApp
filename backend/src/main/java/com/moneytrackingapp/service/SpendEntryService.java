package com.moneytrackingapp.service;

import com.moneytrackingapp.model.SpendEntry;
import java.util.List;

public interface SpendEntryService {
    List<SpendEntry> listAllEntries();
    SpendEntry createEntry(SpendEntry spendEntry);
    SpendEntry updateEntry(Long id, SpendEntry spendEntry);
    void deleteEntry(Long id);
    void deleteAllEntries();
}
