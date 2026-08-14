package com.example.finance.dto;

import com.example.finance.entity.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    @NotNull
    private TransactionType type;
    @NotNull
    private Long categoryId;
    @NotNull
    @PastOrPresent
    private LocalDate transactionDate;
    @Size(max = 1000)
    private String description;
}
