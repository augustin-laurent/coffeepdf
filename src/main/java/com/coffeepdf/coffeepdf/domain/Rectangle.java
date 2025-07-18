package com.coffeepdf.coffeepdf.domain;

/**
 * Represents a rectangle defined by its top-left corner coordinates and dimensions.
 * This class provides methods to get and set the rectangle's position and size.
 */
public class Rectangle {
    // the x-coordinate of the rectangle's top-left corner
    private double x;
    // the y-coordinate of the rectangle's top-left corner
    private double y;
    // the width of the rectangle
    private double width;
    // the height of the rectangle
    private double height;

    /**
     * Creates a rectangle with the specified coordinates and dimensions.
     * @param xArg the x-coordinate of the rectangle's top-left corner
     * @param yArg the y-coordinate of the rectangle's top-left corner
     * @param widthArg the width of the rectangle
     * @param heightArg the height of the rectangle
     */
    public Rectangle(double xArg, double yArg, double widthArg, double heightArg) {
        x = xArg;
        y = yArg;
        width = widthArg;
        height = heightArg;
    }

    /**
     * Returns the x-coordinate of the rectangle's top-left corner.
     * @return the x-coordinate
     */
    public double getX() { return x; }
    /**
     * Returns the y-coordinate of the rectangle's top-left corner.
     * @return the y-coordinate
     */
    public double getY() { return y; }
    /**
     * Returns the height of the rectangle.
     * @return the height
     */
    public double getHeight() { return height; }
    /**
     * Returns the width of the rectangle.
     * @return the width
     */
    public double getWidth() { return width; }

    /**
     * Sets the x-coordinate of the rectangle's top-left corner.
     * @param xArg the new x-coordinate
     */
    public void setX(double xArg) { x = xArg; }
    /**
     * Sets the y-coordinate of the rectangle's top-left corner.
     * @param yArg the new y-coordinate
     */
    public void setY(double yArg) { y = yArg; }
    /**
     * Sets the width of the rectangle.
     * @param widthArg the new width
     */
    public void setWidth(double widthArg) { width = widthArg; }
    /**
     * Sets the height of the rectangle.
     * @param heightArg the new height
     */
    public void setHeight(double heightArg) { height = heightArg; }
}
