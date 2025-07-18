package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;

import java.util.List;
import java.util.Map;

/**
 * Service interface for PDF operations.
 * This interface defines methods for converting images to PDF, deleting pages,
 * merging PDFs, rotating pages, and exporting pages from a PDF document.
 */
public interface PDFService {
    /**
     * Converts a list of image file paths to a PDF document.
     *
     * @param imagePaths List of image file paths to be converted
     * @return PDFDocument containing the converted images
     */
    PDFDocument convertImagesToPDF(List<String> imagePaths);
    /**
     * Deletes specified pages from a PDF document.
     * @param pdf the PDF document from which pages will be deleted
     * @param pages List of page numbers to be deleted (1-based index)
     * @return PDFDocument containing the remaining pages after deletion
     */
    PDFDocument deletePages(PDFDocument pdf, List<Integer> pages);
    /**
     * Rotates specified pages in a PDF document by given angles.
     *
     * @param pdf PDFDocument to be modified
     * @param rotations Map where keys are page numbers (1-based index) and values are rotation angles in degrees
     * @return PDFDocument with the specified pages rotated
     */
    PDFDocument rotatePages(PDFDocument pdf, Map<Integer, Integer> rotations);
    /**
     * Exports specified pages from a PDF document into a new PDF document.
     *
     * @param pdf PDFDocument to be exported
     * @param pages List of page numbers to be exported (1-based index)
     * @return PDFDocument containing the exported pages
     */
    PDFDocument exportPages(PDFDocument pdf, List<Integer> pages);
}
