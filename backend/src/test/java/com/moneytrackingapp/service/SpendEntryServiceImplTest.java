package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.SpendEntry;
import com.moneytrackingapp.repository.SpendEntryRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpendEntryServiceImplTest {
    @Mock
    private SpendEntryRepository repository;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private SpendEntryServiceImpl service;

    @BeforeEach
    void setCurrentUser() {
        when(currentUser.requireUserId()).thenReturn(1L);
    }

    @Test
    void listAllEntriesUsesCurrentUserId() {
        SpendEntry entry = ownedEntry();
        when(repository.findAllByUserIdOrderByDateDescIdDesc(1L)).thenReturn(List.of(entry));

        List<SpendEntry> result = service.listAllEntries();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(repository).findAllByUserIdOrderByDateDescIdDesc(1L);
    }

    @Test
    void createEntryAssignsCurrentUser() {
        when(repository.save(any(SpendEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SpendEntry created = service.createEntry(new SpendEntry());

        assertEquals(1L, created.getUserId());
        verify(repository).save(created);
    }

    @Test
    void updateEntryThrowsWhenNotOwnedByCurrentUser() {
        SpendEntry update = ownedEntry();
        when(repository.findByIdAndUserId(9L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateEntry(9L, update));
    }

    @Test
    void deleteEntryThrowsWhenNotOwnedByCurrentUser() {
        when(repository.findByIdAndUserId(9L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteEntry(9L));
    }

    private SpendEntry ownedEntry() {
        SpendEntry entry = new SpendEntry();
        entry.setId(5L);
        entry.setUserId(1L);
        entry.setDescription("Lunch");
        entry.setCategory("Food");
        entry.setAmount(new BigDecimal("12.50"));
        return entry;
    }
}
