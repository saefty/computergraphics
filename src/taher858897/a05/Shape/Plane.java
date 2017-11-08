package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Plane implements Shape {
    final Vec3 norm_vec;
    final Vec3 position;
    final Material material;

    public Plane(Vec3 postion, Vec3 norm_vec, Material material) {
        this.norm_vec = Vec3.normalize(norm_vec);
        this.position = postion;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 sub_R_origin = Vec3.multiply(-1 ,r.o);
        double up = Vec3.dotProduct(
                Vec3.add(position, sub_R_origin),
                norm_vec);
        double down = Vec3.dotProduct(norm_vec, r.d);
        if (down == 0) return null;
        double t = up/down;
        if (t < r.t0 ) return  null;
        if (t == 0)
            return null;
        return new Hit(t, norm_vec, r.pointAt(t), material);
    }
}
