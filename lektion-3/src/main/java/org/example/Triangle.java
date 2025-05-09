package org.example;

public class Triangle extends Shape implements Rotatable {
    private double side1;
    private double side2;
    private double side3;
    private double rotation = 0;

    public Triangle(String color, double side1, double side2, double side3) {
        super(color);
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    @Override
    public double calculatePerimeter() {
        return side1 + side2 + side3;
    }

    @Override
    public double calculateArea() {
        double s = calculatePerimeter() / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    @Override
    public void rotate(double degrees) {
        rotation = (rotation + degrees) % 360;
        System.out.println("Triangeln har roterats till " + rotation + " grader.");
    }

    @Override
    public String toString() {
        return "Triangle (color=" + getColor() +
                ", sides=" + side1 + ", " + side2 + ", " + side3 +
                ", rotation=" + rotation + "°)";
    }

    public double getSide1() { return side1; }
    public double getSide2() { return side2; }
    public double getSide3() { return side3; }
}
