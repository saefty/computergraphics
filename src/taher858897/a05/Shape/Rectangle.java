package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Rectangle implements Shape {
    final Vec3 position;
    final double width;
    final double depth;
    final Vec3 norm_vec;
    final Material material;

    public Rectangle(Vec3 position, double width, double depth, Material material) {
        this.position = Mat4.translate(position).transformPoint(new Vec3(1,1,1));
        this.width = width;
        this.depth = depth;
        this.material = material;
        this.norm_vec = new Vec3(0,1,0);
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 sub_R_origin = Vec3.multiply(-1, r.o);
        double up = Vec3.dotProduct(
                Vec3.add(position, sub_R_origin),
                norm_vec);
        double down = Vec3.dotProduct(norm_vec, r.d);
        if (down == 0) return null;
        double t = up/down;
        if (t <= r.t0 ) return  null;
        Vec3 point = r.pointAt(t);
        if (Math.abs(point.x) >= width || Math.abs(point.z) >= depth) return null;
        Vec3 norm_vec;
        if (r.d.y < 0)
            norm_vec = new Vec3(0,1,0);
        else
            norm_vec = new Vec3(0,-1,0);
        return new Hit(t, norm_vec, point, material);
    }
}
