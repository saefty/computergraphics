package taher858897.a03;

import cgtools.Vec3;

public class Ray {
    public final Vec3 o;
    public final Vec3 d;
    public final double t0;
    public final double t1;

    public Ray(Vec3 origin, Vec3 normalized_direction, double t0, double t1) {
        this.o = origin;
        this.d = normalized_direction;
        this.t0 = t0;
        this.t1 = t1;
    }

    public Vec3 pointAt(double t){
        return Vec3.add(o, Vec3.multiply(t,d));
    }

    public boolean contains(double t){
        return t >= t0 || t <= t1;
    }
}
