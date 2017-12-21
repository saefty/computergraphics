package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Disk extends Plane {

    final double radius;
    final Vec3 minPos;
    final Vec3 maxPos;

    public Disk(Vec3 postion, Vec3 norm_vec, Material material, double radius) {
        super(postion, norm_vec, material);
        this.radius = radius;
        minPos = new Vec3(postion.x-radius, postion.y-radius, postion.z-radius);
        maxPos = new Vec3(postion.x+radius, postion.y+radius, postion.z+radius);
    }

    @Override
    public Hit intersect(Ray r) {
        Hit h = super.intersect(r);
        if (h != null && Vec3.subtract(position, h.position).length() > radius) return null;
        return h;
    }

    @Override
    public BoundingBox bounds() {
        return new BoundingBox(minPos, maxPos);
    }

    @Override
    public Vec3 getMinPos() {
        return minPos;
    }

    @Override
    public Vec3 getMaxPos() {
        return maxPos;
    }
}
