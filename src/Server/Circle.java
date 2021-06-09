package Server;

import Server.Point;

import java.util.Collection;

public final class Circle {

    private static final double MULTIPLICATIVE_EPSILON = 1 + 1e-14;


    public final Point c;   // Center
    public final double r;  // Radius


    public Circle(Point c, double r) {
        this.c = c;
        this.r = r;
    }


    public boolean contains(Point p) {
        return c.distance(p) <= r * MULTIPLICATIVE_EPSILON;
    }

    public String toString() {
        return String.format("Circle(x=%g, y=%g, r=%g)", c.x, c.y, r);
    }
}
