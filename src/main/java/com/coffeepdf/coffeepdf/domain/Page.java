package com.coffeepdf.coffeepdf.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a page in a PDF document.
 * Each page has a page number, rotation angle, content, and a list of annotations.
 */
public class Page {
    // the page number in the PDF document
    private final int pageNumber;
    // the rotation angle of the page in degrees (0, 90, 180, or 270)
    private int rotationAngle;
    // the content of the page, typically in byte array format
    private byte[] content;
    // a list of annotations associated with the page
    private final List<Annotation> annotations = new ArrayList<>();

    /**
     * Creates a page with the specified page number and content.
     *
     * @param pageNumberArg the page number in the PDF document
     * @param rotationAngleArg the rotation angle of the page in degrees (0, 90, 180, or 270)
     * @param contentArg    the content of the page in byte array format
     */
    public Page(int pageNumberArg, int rotationAngleArg, byte[] contentArg) {
        pageNumber = pageNumberArg;
        content = contentArg;
        rotationAngle = rotationAngleArg;
    }

    /**
     * Creates a page with the specified page number and content.
     *
     * @param pageNumberArg the page number in the PDF document
     * @param contentArg    the content of the page in byte array format
     */
    public Page(int pageNumberArg, byte[] contentArg) {
        pageNumber = pageNumberArg;
        content = contentArg;
        rotationAngle = 0;
    }

    /**
     * Returns the page number of the page.
     *
     * @return the page number
     */
    public int getPageNumber() { return pageNumber; }
    /**
     * Returns the rotation angle of the page in degrees.
     *
     * @return the rotation angle (0, 90, 180, or 270)
     */
    public int getRotationAngle() { return rotationAngle; }
    /**
     * Returns the content of the page.
     *
     * @return the content as a byte array
     */
    public byte[] getContent() { return content; }
    /**
     * Returns the list of annotations associated with the page.
     *
     * @return the list of annotations
     */
    public List<Annotation> getAnnotations() { return annotations; }

    /**
     * Sets the content of the page.
     *
     * @param contentArg the new content of the page in byte array format
     */
    public void setContent(byte[] contentArg) { content = contentArg; }

    /**
     * Rotates the page by the specified angle.
     * The angle is added to the current rotation angle and normalized to be within 0-359 degrees.
     *
     * @param angleArg the angle in degrees to rotate the page (can be positive or negative)
     */
    public void setRotationAngle(int angleArg) {
        rotationAngle = (rotationAngle + angleArg) % 360;
    }

    /**
     * Adds an annotation to the page.
     *
     * @param annotation the annotation to add
     */
    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    /**
     * Removes an annotation from the page.
     *
     * @param annotation the annotation to remove
     */
    public void removeAnnotation(Annotation annotation) {
        annotations.remove(annotation);
    }

    /**
     * Clears the content of the page.
     * This method sets the content to null, effectively removing any existing content.
     */
    public void clearContent() {
        content = null;
    }
}
