package org.example;

public class Circle extends Shape {
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

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle (color=" + getColor() + ", radius=" + radius + ")";
    }
}
