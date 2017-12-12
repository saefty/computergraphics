package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Disk  extends Plane {

    final double radius;

    public Disk(Vec3 postion, Vec3 norm_vec, Material material, double radius) {
        super(postion, norm_vec, material);
        this.radius = radius;
    }

    @Override
    public Hit intersect(Ray r) {
        Hit h = super.intersect(r);
        if (h != null && Vec3.subtract(position, h.position).length() > radius) return null;
        return h;
    }
}
