package org.example;

public abstract class Shape {
    private String color;

    // Konstruktor
    public Shape(String color) {
        this.color = color;
    }

    // Getter
    public String getColor() {
        return color;
    }

    // Setter
    public void setColor(String color) {
        this.color = color;
    }

    // Abstrakta metoder – måste implementeras av subklasser
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
}
