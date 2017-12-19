package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Background implements Shape {
    public final Material material;

    public static final Vec3 MIN_POS = new Vec3(Double.NEGATIVE_INFINITY);
    public static final Vec3 MAX_POS = new Vec3(Double.POSITIVE_INFINITY);

    public Background(Material material) {
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        if (r.t1 < Double.POSITIVE_INFINITY) return null;
        return new Hit(Double.POSITIVE_INFINITY, Vec3.zero, Vec3.zero, material);
    }

    @Override
    public Vec3 getMinPos() {
        return MIN_POS;
    }

    @Override
    public Vec3 getMaxPos() {
        return MAX_POS;
    }

}
