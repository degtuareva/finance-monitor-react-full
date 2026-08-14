package com.example.finance.dto;

import com.example.finance.entity.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private TransactionType type;
}
