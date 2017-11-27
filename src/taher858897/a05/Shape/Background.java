package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Background implements Shape {
    public final Material material;

    public Background(Material material) {
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        if (r.t1 < Double.POSITIVE_INFINITY) return null;
        return new Hit(Double.POSITIVE_INFINITY, Vec3.zero, Vec3.zero, material);
    }

}
