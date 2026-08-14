package com.example.finance.dto;

import com.example.finance.entity.ReportFormat;
import com.example.finance.entity.ReportPeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequest {
    @NotNull
    private ReportPeriod period;
    private LocalDate from;
    private LocalDate to;
    @NotNull
    private ReportFormat format;

    public LocalDate resolvedFrom() {
        LocalDate now = LocalDate.now();
        if (period == ReportPeriod.CUSTOM) {
            if (from == null) throw new IllegalArgumentException("Укажите дату начала");
            return from;
        }
        if (period == ReportPeriod.MONTH) return now.withDayOfMonth(1);
        if (period == ReportPeriod.QUARTER)
            return now.withMonth(((now.getMonthValue() - 1) / 3) * 3 + 1).withDayOfMonth(1);
        return now.withDayOfYear(1);
    }

    public LocalDate resolvedTo() {
        if (period == ReportPeriod.CUSTOM) {
            if (to == null) throw new IllegalArgumentException("Укажите дату окончания");
            return to;
        }
        return LocalDate.now();
    }
}
