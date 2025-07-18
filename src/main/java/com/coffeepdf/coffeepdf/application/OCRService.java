package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;

import java.util.Map;

/**
 * Service interface for Optical Character Recognition (OCR) operations on PDF documents.
 * Provides methods to extract text from PDF documents and individual pages.
 */
public interface OCRService {
    /**
     * Extracts text from all pages of a PDF document.
     *
     * @param pdf the PDF document from which text will be extracted
     * @return a map where keys are page numbers (1-based index) and values are the extracted text for each page
     */
    Map<Integer, String> extractText(PDFDocument pdf);
    /**
     * Extracts text from a specific page of a PDF document.
     *
     * @param page the page from which text will be extracted
     * @return the extracted text as a String
     */
    String extractTextFromPage(Page page);
}
