package com.coffeepdf.coffeepdf.presentation;

import com.coffeepdf.coffeepdf.application.PDFService;
import com.coffeepdf.coffeepdf.application.PDFServiceImpl;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.infrastructure.PDFRepository;
import com.coffeepdf.coffeepdf.infrastructure.PDFRepositoryImpl;

import java.util.List;
import java.util.Map;

public class PDFController {
    private final PDFService pdfService;
    private final PDFRepository pdfRepository;
    private PDFDocument currentDocument;

    public PDFController() {
        pdfService = new PDFServiceImpl();
        pdfRepository = new PDFRepositoryImpl();
    }

    public PDFController(PDFService pdfServiceArg, PDFRepository pdfRepositoryArg) {
        pdfService = pdfServiceArg;
        pdfRepository = pdfRepositoryArg;
    }

    public PDFDocument rotatePages(PDFDocument doc, Map<Integer, Integer> rotations) {
        return pdfService.rotatePages(doc, rotations);
    }

    public PDFDocument deletePages(PDFDocument doc, List<Integer> pages) {
        return pdfService.deletePages(doc, pages);
    }

    public PDFDocument getCurrentDocument() {
        return currentDocument;
    }

    public void handleOpenPDF(String path) {
        currentDocument = pdfRepository.load(path);
    }

    public void handleSavePDF(String path) {
        if (currentDocument != null) {
            pdfRepository.save(currentDocument, path);
        }
    }

    public void handleDeletePages(List<Integer> pages) {
        if (currentDocument != null) {
            currentDocument = pdfService.deletePages(currentDocument, pages);
        }
    }

    public void handleRotatePages(Map<Integer, Integer> rotations) {
        if (currentDocument != null) {
            currentDocument = pdfService.rotatePages(currentDocument, rotations);
        }
    }

    public void handleExportPages(List<Integer> pages, String exportPath) {
        if (currentDocument != null) {
            PDFDocument exported = pdfService.exportPages(currentDocument, pages);
            pdfRepository.save(exported, exportPath);
        }
    }
}
