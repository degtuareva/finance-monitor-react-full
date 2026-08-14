package com.example.finance.service;

import com.example.finance.dto.TransactionRequest;
import com.example.finance.entity.Category;
import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionType;
import com.example.finance.entity.User;
import com.example.finance.repository.CategoryRepository;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository tx;

    @Mock
    private CategoryRepository categories;

    @Mock
    private UserRepository users;

    @InjectMocks
    private TransactionService service;

    @Test
    void createsTransaction() {
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setType(TransactionType.EXPENSE);

        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("10.00"));
        request.setType(TransactionType.EXPENSE);
        request.setCategoryId(2L);
        request.setTransactionDate(LocalDate.now());

        Mockito.when(users.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(categories.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(category));
        Mockito.when(tx.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction result = service.create(1L, request);

        assertEquals(new BigDecimal("10.00"), result.getAmount());
        Mockito.verify(tx).save(any(Transaction.class));
    }

    @Test
    void rejectsWrongCategoryType() {
        Category category = new Category();
        category.setType(TransactionType.INCOME);

        TransactionRequest request = new TransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setCategoryId(2L);

        // stubbing только для категории, users не нужен в этом сценарии
        Mockito.when(categories.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(category));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.create(1L, request)
        );

        // дополнительно проверяем, что save не вызван
        Mockito.verify(tx, Mockito.never()).save(any());
    }
}