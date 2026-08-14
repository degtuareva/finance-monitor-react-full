package com.example.finance.service;

import com.example.finance.dto.TransactionRequest;
import com.example.finance.entity.Category;
import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionType;
import com.example.finance.repository.CategoryRepository;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
    private final TransactionRepository tx;
    private final CategoryRepository categories;
    private final UserRepository users;

    @Transactional(readOnly = true)
    public List<Transaction> list(Long u, LocalDate f, LocalDate t) {
        return tx.findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(u, f, t);
    }

    public Transaction create(Long u, TransactionRequest r) {
        return apply(u, new Transaction(), r);
    }

    public Transaction update(Long u, Long id, TransactionRequest r) {
        return apply(u, tx.findByIdAndUserId(id, u).orElseThrow(() -> new IllegalArgumentException("Транзакция не найдена")), r);
    }

    private Transaction apply(Long u, Transaction t, TransactionRequest r) {
        Category c = categories.findByIdAndUserId(r.getCategoryId(), u).orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        if (c.getType() != r.getType()) throw new IllegalArgumentException("Тип транзакции не соответствует категории");
        t.setUser(users.findById(u).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден")));
        t.setCategory(c);
        t.setAmount(r.getAmount());
        t.setType(r.getType());
        t.setTransactionDate(r.getTransactionDate());
        t.setDescription(r.getDescription());
        return tx.save(t);
    }

    public void delete(Long u, Long id) {
        tx.delete(tx.findByIdAndUserId(id, u).orElseThrow(() -> new IllegalArgumentException("Транзакция не найдена")));
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> metrics(Long u, LocalDate f, LocalDate t) {
        BigDecimal i = tx.sum(u, TransactionType.INCOME, f, t), e = tx.sum(u, TransactionType.EXPENSE, f, t);
        return Map.of("income", i, "expense", e, "balance", i.subtract(e));
    }
}
