package com.coffeepdf.coffeepdf.domain;

import java.util.*;

/**
 * Represents a PDF document containing multiple pages, annotations, and signatures.
 * Each document has a unique identifier and a name.
 */
public class PDFDocument {
    // Unique identifier for the PDF document
    private final UUID id;
    // Name of the PDF document
    private final String name;
    // List of pages in the PDF document
    private final List<Page> pages = new ArrayList<>();
    // List of annotations in the PDF document
    private final List<Annotation> annotations = new ArrayList<>();
    // List of signatures in the PDF document
    private final List<Signature> signatures = new ArrayList<>();
    // Source path of the PDF document, used for loading and saving
    private final String sourcePath;

    /**
     * Creates a PDF document with the specified unique identifier and name.
     *
     * @param idArg   the unique identifier for the PDF document
     * @param nameArg the name of the PDF document
     */
    public PDFDocument(UUID idArg, String nameArg) {
        id = idArg;
        name = nameArg;
        sourcePath = null;
    }

    /**
     * Creates a PDF document with the specified unique identifier and name and original path.
     *
     * @param idArg   the unique identifier for the PDF document
     * @param nameArg the name of the PDF document
     * @param sourcePathArg the path to the PDF document
     */
    public PDFDocument(UUID idArg, String nameArg, String sourcePathArg) {
        id = idArg;
        name = nameArg;
        sourcePath = sourcePathArg;
    }

    /**
     * Returns the unique identifier of the PDF document.
     *
     * @return the UUID of the PDF document
     */
    public UUID getId() { return id; }
    /**
     * Returns the name of the PDF document.
     *
     * @return the name as a String
     */
    public String getName() { return name; }
    /**
     * Returns the list of pages in the PDF document.
     *
     * @return the list of Page objects
     */
    public List<Page> getPages() { return pages; }
    /**
     * Returns the list of annotations in the PDF document.
     *
     * @return the list of Annotation objects
     */
    public List<Annotation> getAnnotations() { return annotations; }
    /**
     * Returns the list of signatures in the PDF document.
     *
     * @return the list of Signature objects
     */
    public List<Signature> getSignatures() { return signatures; }

    /**
     * Adds a new page to the PDF document.
     *
     * @param page the Page object to be added
     */
    public void addPage(Page page) {
        pages.add(page);
    }

    /**
     * Removes a page from the PDF document by its page number.
     *
     * @param pageNumber the page number of the page to be removed
     */
    public void removePage(int pageNumber) {
        pages.removeIf(page -> page.getPageNumber() == pageNumber);
    }

    /**
     * Retrieves a page from the PDF document by its page number.
     *
     * @param pageNumber the page number of the page to retrieve
     * @return the Page object if found, or null if not found
     */
    public Page getPage(int pageNumber) {
        return pages.stream().filter(page -> page.getPageNumber() == pageNumber).findFirst().orElse(null);
    }

    /**
     * Adds an annotation to the PDF document.
     *
     * @param annotation the Annotation object to be added
     */
    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    /**
     * Removes an annotation from the PDF document by its unique identifier.
     *
     * @param annotationId the unique identifier of the annotation to be removed
     */
    public void removeAnnotation(UUID annotationId) {
        annotations.removeIf(annotation -> annotation.getId().equals(annotationId));
    }

    /**
     * Adds a signature to the PDF document.
     *
     * @param signature the Signature object to be added
     */
    public void addSignature(Signature signature) {
        signatures.add(signature);
    }

    /**
     * Return the source path of the PDF document.
     *
     * @return the source path as a String
     */
    public String getSourcePath() {
        return sourcePath;
    }
}