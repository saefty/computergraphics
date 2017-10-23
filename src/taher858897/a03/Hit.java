package taher858897.a03;

import cgtools.Vec3;

public class Hit {
    public final double t;
    public final Vec3 normVec;
    public final Vec3 position;

    public Hit(double t, Vec3 normVec, Vec3 position) {
        this.t = t;
        this.normVec = normVec;
        this.position = position;
    }
}
