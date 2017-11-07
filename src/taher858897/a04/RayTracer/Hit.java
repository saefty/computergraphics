package taher858897.a04.RayTracer;

import cgtools.Vec3;

public class Hit {
    public final double t;
    public final Vec3 normVec;
    public final Vec3 position;
    public final Vec3 color;

    public Hit(double t, Vec3 normVec, Vec3 position, Vec3 color) {
        this.t = t;
        this.normVec = normVec;
        this.position = position;
        this.color = color;
    }
}
