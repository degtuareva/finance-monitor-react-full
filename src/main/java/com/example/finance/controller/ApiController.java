package com.example.finance.controller;

import com.example.finance.dto.CategoryRequest;
import com.example.finance.dto.ReportRequest;
import com.example.finance.dto.TransactionRequest;
import com.example.finance.entity.ReportFormat;
import com.example.finance.security.CustomUserPrincipal;
import com.example.finance.service.CategoryService;
import com.example.finance.service.ReportService;
import com.example.finance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final TransactionService tx;
    private final CategoryService categories;
    private final ReportService reports;

    @GetMapping("/transactions")
    public Object list(@AuthenticationPrincipal CustomUserPrincipal p, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return tx.list(p.getId(), from, to);
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> create(@AuthenticationPrincipal CustomUserPrincipal p, @Valid @RequestBody TransactionRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tx.create(p.getId(), r));
    }

    @PutMapping("/transactions/{id}")
    public Object update(@AuthenticationPrincipal CustomUserPrincipal p, @PathVariable Long id, @Valid @RequestBody TransactionRequest r) {
        return tx.update(p.getId(), id, r);
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserPrincipal p, @PathVariable Long id) {
        tx.delete(p.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public Object categories(@AuthenticationPrincipal CustomUserPrincipal p) {
        return categories.list(p.getId());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> category(@AuthenticationPrincipal CustomUserPrincipal p, @Valid @RequestBody CategoryRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categories.create(p.getId(), r));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> categoryDelete(@AuthenticationPrincipal CustomUserPrincipal p, @PathVariable Long id) {
        categories.delete(p.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/metrics")
    public Object metrics(@AuthenticationPrincipal CustomUserPrincipal p, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return tx.metrics(p.getId(), from, to);
    }

    @GetMapping("/reports/preview")
    public Object preview(@AuthenticationPrincipal CustomUserPrincipal p, @Valid ReportRequest r) {
        return reports.preview(p.getId(), r);
    }

    @GetMapping("/reports/export")
    public ResponseEntity<byte[]> report(@AuthenticationPrincipal CustomUserPrincipal p, @Valid ReportRequest r) throws Exception {
        byte[] body = reports.create(p.getId(), r);
        String ext = r.getFormat() == ReportFormat.PDF ? "pdf" : "xlsx";
        MediaType type = r.getFormat() == ReportFormat.PDF ? MediaType.APPLICATION_PDF : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity.ok().contentType(type).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report." + ext).body(body);
    }
}
