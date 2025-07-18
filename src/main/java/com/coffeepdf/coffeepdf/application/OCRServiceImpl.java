package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class OCRServiceImpl implements OCRService {

    @Override
    public Map<Integer, String> extractText(PDFDocument pdf) {
        return pdf.getPages().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Page::getPageNumber,
                        this::extractTextFromPage
                ));
    }

    @Override
    public String extractTextFromPage(Page page) {
        // TODO : Implement OCR logic here
        return "";
    }
}
