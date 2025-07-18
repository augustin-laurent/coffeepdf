package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.Annotation;
import com.coffeepdf.coffeepdf.domain.PDFDocument;

import java.util.UUID;

/**
 * Service interface for managing annotations in PDF documents.
 * Provides methods to add, edit, and remove annotations.
 */
public interface AnnotationService {
    /**
     * Adds an annotation to a PDF document.
     *
     * @param pdf        the PDF document to which the annotation will be added
     * @param annotation the annotation to be added
     */
    void addAnnotation(PDFDocument pdf, Annotation annotation);
    /**
     * Edits an existing annotation in a PDF document.
     *
     * @param pdf          the PDF document containing the annotation
     * @param annotationId the unique identifier of the annotation to be edited
     * @param newContent   the new content for the annotation
     */
    void editAnnotation(PDFDocument pdf, UUID annotationId, String newContent);
    /**
     * Removes an annotation from a PDF document.
     *
     * @param pdf          the PDF document from which the annotation will be removed
     * @param annotationId the unique identifier of the annotation to be removed
     */
    void removeAnnotation(PDFDocument pdf, UUID annotationId);
}
