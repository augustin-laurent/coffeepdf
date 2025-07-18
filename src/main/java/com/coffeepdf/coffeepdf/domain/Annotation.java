package com.coffeepdf.coffeepdf.domain;

import java.util.UUID;

/**
 * Represents an annotation in a PDF document.
 * An annotation can be a highlight, comment, or shape, and is defined by its type,
 * page number, content, and coordinates.
 */
public class Annotation {
    // Unique identifier for the annotation
    private final UUID id;
    // Type of the annotation (e.g., highlight, comment, shape)
    private final AnnotationType type;
    // Page number where the annotation is located
    private final int pageNumber;
    // Content of the annotation (e.g., text for comments)
    private String content;
    // Coordinates defining the position and size of the annotation
    private Rectangle coordinates;

    /**
     * Creates an annotation with the specified parameters.
     *
     * @param idArg         the unique identifier for the annotation
     * @param typeArg       the type of the annotation (e.g., highlight, comment, shape)
     * @param pageNumberArg the page number where the annotation is located
     * @param contentArg    the content of the annotation
     * @param coordinatesArg the rectangle defining the coordinates of the annotation
     */
    public Annotation(UUID idArg, AnnotationType typeArg, int pageNumberArg, String contentArg, Rectangle coordinatesArg) {
        id = idArg;
        type = typeArg;
        pageNumber = pageNumberArg;
        content = contentArg;
        coordinates = coordinatesArg;
    }

    /**
     * Returns the unique identifier of the annotation.
     *
     * @return the UUID of the annotation
     */
    public UUID getId() { return id; }
    /**
     * Returns the type of the annotation.
     *
     * @return the AnnotationType of the annotation
     */
    public AnnotationType getType() { return type; }
    /**
     * Returns the page number where the annotation is located.
     *
     * @return the page number
     */
    public int getPageNumber() { return pageNumber; }
    /**
     * Returns the content of the annotation.
     *
     * @return the content as a String
     */
    public String getContent() { return content; }
    /**
     * Returns the coordinates of the annotation.
     *
     * @return the Rectangle defining the coordinates
     */
    public Rectangle getCoordinates() { return coordinates; }

    /**
     * Sets the content of the annotation.
     *
     * @param contentArg the new content to set
     */
    public void setContent(String contentArg) { content = contentArg; }
    /**
     * Sets the coordinates of the annotation.
     *
     * @param coordinatesArg the new Rectangle defining the coordinates
     */
    public void setCoordinates(Rectangle coordinatesArg) { coordinates = coordinatesArg; }
}
