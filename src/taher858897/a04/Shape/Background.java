package taher858897.a04.Shape;

import cgtools.Vec3;
import taher858897.a04.RayTracer.Hit;
import taher858897.a04.RayTracer.Ray;
import taher858897.a04.Shape.Shape;

public class Background implements Shape {
    public final Vec3 color;

    public Background(Vec3 color) {
        this.color = color;
    }

    @Override
    public Hit intersect(Ray r) {
        if (r.t0 < 0 || r.t1 < Double.MAX_VALUE) return null;
        return new Hit(Double.MAX_VALUE, new Vec3(0,0,1), new Vec3(0,0, Double.MAX_VALUE), color);
    }
}
