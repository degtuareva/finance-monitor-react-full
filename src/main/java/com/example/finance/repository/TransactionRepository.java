package com.example.finance.repository;

import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(Long userId, LocalDate from, LocalDate to);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    @Query("select coalesce(sum(t.amount),0) from Transaction t where t.user.id=:u and t.type=:type and t.transactionDate between :f and :to")
    BigDecimal sum(@Param("u") Long u, @Param("type") TransactionType type, @Param("f") LocalDate f, @Param("to") LocalDate t);
}
