package com.coffeepdf.coffeepdf.infrastructure;

import com.coffeepdf.coffeepdf.domain.PDFDocument;

public interface PDFRepository {
    PDFDocument load(String path);
    void save(PDFDocument document, String path);
}
