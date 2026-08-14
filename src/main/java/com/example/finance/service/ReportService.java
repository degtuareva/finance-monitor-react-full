package com.example.finance.service;

import com.example.finance.dto.ReportRequest;
import com.example.finance.entity.ReportFormat;
import com.example.finance.entity.Transaction;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final TransactionService tx;

    public List<Transaction> preview(Long u, ReportRequest r) {
        return tx.list(u, r.resolvedFrom(), r.resolvedTo());
    }

    public byte[] create(Long u, ReportRequest r) throws Exception {
        return r.getFormat() == ReportFormat.PDF ? pdf(u, r) : xlsx(u, r);
    }

    private byte[] pdf(Long u, ReportRequest r) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document d = new Document();
        PdfWriter.getInstance(d, out);
        d.open();
        d.add(new Paragraph("Financial report"));
        for (Transaction t : preview(u, r))
            d.add(new Paragraph(t.getTransactionDate() + " | " + t.getType() + " | " + t.getCategory().getName() + " | " + t.getAmount()));
        d.close();
        return out.toByteArray();
    }

    private byte[] xlsx(Long u, ReportRequest r) throws Exception {
        try (Workbook w = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet s = w.createSheet("Transactions");
            String[] h = {"Date", "Type", "Category", "Amount", "Description"};
            Row row = s.createRow(0);
            for (int i = 0; i < h.length; i++) row.createCell(i).setCellValue(h[i]);
            int n = 1;
            for (Transaction t : preview(u, r)) {
                Row x = s.createRow(n++);
                x.createCell(0).setCellValue(t.getTransactionDate().toString());
                x.createCell(1).setCellValue(t.getType().name());
                x.createCell(2).setCellValue(t.getCategory().getName());
                x.createCell(3).setCellValue(t.getAmount().doubleValue());
                x.createCell(4).setCellValue(t.getDescription() == null ? "" : t.getDescription());
            }
            w.write(out);
            return out.toByteArray();
        }
    }
}
