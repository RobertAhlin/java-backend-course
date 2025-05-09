package org.example;

public class Circle extends Shape implements Resizable {
    private double radius;

    public Circle(String color, double radius) {
        super(color);  // skicka färgen till Shape
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
        System.out.println("Cirkeln har ändrats i storlek. Ny radie: " + radius);
    }


    @Override
    public String toString() {
        return "Circle (color=" + getColor() + ", radius=" + radius + ")";
    }


}
