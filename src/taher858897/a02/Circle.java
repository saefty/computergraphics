package taher858897.a02;

import cgtools.Vec3;

import java.util.HashMap;

public class Circle {
    private double x;
    private double y;
    private double radius;
    private Vec3 color;

    public Circle(double x, double y, double radius, Vec3 color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public boolean coordinateInCircle(double x_in, double y_in) {
        return Math.sqrt(Math.pow(x - x_in, 2) + Math.pow(y - y_in, 2)) <= radius;
    }

    public Vec3 getColor() {
        return color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
