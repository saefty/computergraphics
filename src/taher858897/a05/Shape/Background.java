package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.normalize;
import static cgtools.Vec3.zero;

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
        Vec3 normal = normalize(r.d);
        double inclination = Math.acos(normal.y);
        double azimuth = Math.PI + Math.atan2(normal.x, normal.z);
        double u = azimuth / (2 * Math.PI);
        double v = inclination / Math.PI;
        return new Hit(Double.POSITIVE_INFINITY, Vec3.zero, Vec3.zero, new Vec3(u,v,0), material);
    }

    @Override
    public BoundingBox bounds() {
        return new BoundingBox(MIN_POS, MAX_POS);
    }

    @Override
    public boolean contains(Vec3 pos) {
        return false;
    }

}
