package org.example;

public class Rectangle extends Shape implements Resizable, Rotatable {
    private double width;
    private double height;
    private double rotation = 0;  // vinkel i grader

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }

    @Override
    public void resize(double factor) {
        width *= factor;
        height *= factor;
        System.out.println("Rektangeln har ändrats i storlek. Ny bredd: " + width + ", Ny höjd: " + height);
    }

    @Override
    public void rotate(double degrees) {
        rotation = (rotation + degrees) % 360;
        System.out.println("Rektangeln har roterats till " + rotation + " grader.");
    }

    @Override
    public String toString() {
        return "Rectangle (color=" + getColor() + ", width=" + width + ", height=" + height + ", rotation=" + rotation + "°)";
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
