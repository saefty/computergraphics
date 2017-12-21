package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Plane implements Shape {
    final Vec3 norm_vec;
    final Vec3 position;
    final Material material;

    public static final Vec3 MIN_POS = new Vec3(Double.NEGATIVE_INFINITY);
    public static final Vec3 MAX_POS = new Vec3(Double.POSITIVE_INFINITY);

    public Plane(Vec3 postion, Vec3 norm_vec, Material material) {
        this.norm_vec = Vec3.normalize(norm_vec);
        this.position = postion;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) {
        double up = Vec3.dotProduct(
                Vec3.subtract(position, r.o),
                norm_vec);
        double down = Vec3.dotProduct(norm_vec, r.d);
        if (down == 0) return null;
        double t = up/down;
        if (t <= r.t0 ) return  null;
        Vec3 norm = norm_vec;
        if (Vec3.dotProduct(norm_vec, r.d) > 0){
            norm = Vec3.multiply(-1, norm_vec);
        }
        return new Hit(t, norm, r.pointAt(t), material);
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox bb = new BoundingBox();
        bb = bb.extend(MIN_POS);
        bb = bb.extend(MAX_POS);
        return bb;
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
