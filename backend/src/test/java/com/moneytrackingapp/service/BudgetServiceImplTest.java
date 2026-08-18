package com.moneytrackingapp.service;

import com.moneytrackingapp.exception.ResourceNotFoundException;
import com.moneytrackingapp.model.Budget;
import com.moneytrackingapp.repository.BudgetRepository;
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
class BudgetServiceImplTest {
    @Mock
    private BudgetRepository repository;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private BudgetServiceImpl service;

    @BeforeEach
    void setCurrentUser() {
        when(currentUser.requireUserId()).thenReturn(1L);
    }

    @Test
    void listAllBudgetsUsesCurrentUserId() {
        Budget budget = ownedBudget();
        when(repository.findAllByUserIdOrderByIdAsc(1L)).thenReturn(List.of(budget));

        List<Budget> result = service.listAllBudgets();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(repository).findAllByUserIdOrderByIdAsc(1L);
    }

    @Test
    void createBudgetAssignsCurrentUser() {
        when(repository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Budget created = service.createBudget(new Budget());

        assertEquals(1L, created.getUserId());
        verify(repository).save(created);
    }

    @Test
    void updateBudgetThrowsWhenNotOwnedByCurrentUser() {
        when(repository.findByIdAndUserId(9L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateBudget(9L, ownedBudget()));
    }

    @Test
    void deleteBudgetThrowsWhenNotOwnedByCurrentUser() {
        when(repository.findByIdAndUserId(9L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteBudget(9L));
    }

    private Budget ownedBudget() {
        Budget budget = new Budget();
        budget.setId(3L);
        budget.setUserId(1L);
        budget.setCategory("Food");
        budget.setAmount(new BigDecimal("100.00"));
        budget.setPeriod("MONTHLY");
        return budget;
    }
}
