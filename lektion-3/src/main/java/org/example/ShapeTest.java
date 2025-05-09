package org.example;

import java.util.ArrayList;
import java.util.List;

public class ShapeTest {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();

        shapes.add(new Circle("Röd", 5));
        shapes.add(new Rectangle("Blå", 4, 6));
        shapes.add(new Triangle("Lila", 3,4, 5));

        for (Shape s : shapes) {
            System.out.println(s);
            System.out.println("Färg: " + s.getColor());
            System.out.println("Area: " + s.calculateArea());
            System.out.println("Omkrets: " + s.calculatePerimeter());
            System.out.println("---");
        }

        List<Resizable> resizables = new ArrayList<>();
        resizables.add(new Circle("Gul", 3));
        resizables.add(new Rectangle("Grå", 2, 4));

        for (Resizable r : resizables) {
            r.resize(1.5);
        }

        List<Rotatable> rotatables = new ArrayList<>();
        rotatables.add(new Rectangle("Grön", 3, 6));
        rotatables.add(new Triangle("Lila", 3, 4, 5));

        for (Rotatable r : rotatables) {
            r.rotate(90);
        }


    }
}
