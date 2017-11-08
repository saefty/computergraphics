package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.Material.Material;

public class Hit {
    public final double t;
    public final Vec3 normVec;
    public final Vec3 position;
    public final Material material;

    public Hit(double t, Vec3 normVec, Vec3 position, Material material) {
        this.t = t;
        this.normVec = normVec;
        this.position = position;
        this.material = material;
    }
}
