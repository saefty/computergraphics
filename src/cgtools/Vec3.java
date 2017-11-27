/**
 * A simple 3D vector class. Vector objects are immutable.
 *
 * @author henrik
 */
package cgtools;

import java.io.Serializable;

public class Vec3 implements Serializable {
    public double x, y, z;

    /** Creates a new vector with the given coordinates */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(double v) {
        this.x = v;
        this.y = v;
        this.z = v;
    }

    /** Creates a new vector with the given coordinates */
    public static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    /** Creates a new vector with the given coordinates */
    public static Vec3 vec3(double v) {
        return new Vec3(v);
    }

    /** Adds the given vectors and returns a newly allocated vector. */
    public static Vec3 add(Vec3 a, Vec3... vs) {
        Vec3 r = a;
        for (Vec3 v : vs) {
            r = vec3(r.x + v.x, r.y + v.y, r.z + v.z);
        }
        return r;
    }

    /** Adds the given vectors and returns the first vector. */
    public static Vec3 addFast(Vec3 a, Vec3... vs) {
        double x = 0,
               y = 0,
               z = 0;
        for (Vec3 v : vs) {
            x += v.x;
            y += v.y;
            z += v.z;
        }
        a.x += x;
        a.y += y;
        a.z += z;
        return a;
    }

    /** Subtracts the first vector from the second and returns a newly allocated
     * vector. */
    public static Vec3 subtract(Vec3 a, Vec3 b) {
        return vec3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /** Multiplies the given vectors component-wise and returns a newly
     * allocated result vector. */
    public static Vec3 multiply(Vec3 a, Vec3... vs) {
        Vec3 r = a;
        for (Vec3 v : vs) {
            r = vec3(r.x * v.x, r.y * v.y, r.z * v.z);
        }
        return r;
    }

    /** Multplies the given vector with the scalar value. */
    public static Vec3 multiply(double s, Vec3 a) {
        return vec3(s * a.x, s * a.y, s * a.z);
    }
    public static Vec3 multiplyFast(double s, Vec3 a) {
        a.x *= s;
        a.y *= s;
        a.z *= s;
        return a;
    }

    /** Divides the given vector by the scalar value. */
    public static Vec3 divide(Vec3 a, double s) {
        return vec3(a.x / s, a.y / s, a.z / s);
    }

    public static Vec3 divideFast(Vec3 a, double s) {
        a.x /= s;
        a.y /= s;
        a.z /= s;
        return a;
    }

    /** Calculates the dot product. */
    public static double dotProduct(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /** Calculates the cross product between the two provided vectors. */
    public static Vec3 crossProduct(Vec3 a, Vec3 b) {
        return vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    /** Returns the length of this vector. */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /** Returns the length of the provided vector. */
    public static double length(Vec3 a) {
        return a.length();
    }

    /** Returns the squared length of this vector. */
    public double squaredLength() {
        return x * x + y * y + z * z;
    }

    /** Returns the squared lenght of the provided vector. */
    public static double squaredLength(Vec3 a) {
        return a.squaredLength();
    }

    /** Returns a normalized copy of the provided vector */
    public static Vec3 normalize(Vec3 a) {
        return divide(a, a.length());
    }

    public static Vec3 normalizeFast(Vec3 a) {
        double l = a.length();
        a.x /= l;
        a.y /= l;
        a.z /= l;
        return a;
    }

    @Override
    public String toString() {
        return String.format("(Vec3: %.2f %.2f %.2f)", x, y, z);
    }

    public boolean lessThan(Vec3 v) {
        return x < v.x && y < v.y && z < v.z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vec3))
            return false;
        if (o == this)
            return true;
        Vec3 v = (Vec3) o;
        return v.x == x && v.y == y && v.z == z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Double.valueOf(x).hashCode();
        result = prime * result + Double.valueOf(y).hashCode();
        result = prime * result + Double.valueOf(z).hashCode();
        return result;
    }

    public static Vec3 clamp(Vec3 v) {
        return vec3(Math.min(1, Math.max(v.x, 0)), Math.min(1, Math.max(v.y, 0)), Math.min(1, Math.max(v.z, 0)));
    }

    public static Vec3 hue(double h) {
        double r = Math.abs(h * 6 - 3) - 1;
        double g = 2 - Math.abs(h * 6 - 2);
        double b = 2 - Math.abs(h * 6 - 4);
        return clamp(vec3(r, g, b));
    }

    public static Vec3 hsvToRgb(Vec3 hsv) {
        return multiply(hsv.z, add(multiply(hsv.y, subtract(hue(hsv.x), one)), one));
    }

    public static final Vec3 zero = vec3(0, 0, 0);
    public static final Vec3 one = vec3(1, 1, 1);
    public static final Vec3 black = vec3(0, 0, 0);
    public static final Vec3 white = vec3(1, 1, 1);
    public static final Vec3 red = vec3(1, 0, 0);
    public static final Vec3 green = vec3(0, 1, 0);
    public static final Vec3 blue = vec3(0, 0, 1);
}
