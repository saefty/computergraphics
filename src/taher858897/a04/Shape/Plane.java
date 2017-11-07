package taher858897.a04.Shape;

import cgtools.Vec3;
import taher858897.a04.RayTracer.Hit;
import taher858897.a04.RayTracer.Ray;

public class Plane implements Shape {
    final Vec3 norm_vec;
    final Vec3 position;
    final Vec3 color;

    public Plane(Vec3 postion, Vec3 norm_vec, Vec3 color) {
        this.norm_vec = norm_vec;
        this.position = postion;
        this.color = color;
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 tmpX0 = new Vec3(r.o.x - position.x, r.o.y - position.y,r.o.z - position.z);
        double up = - Vec3.dotProduct(norm_vec, tmpX0);
        double down = Vec3.dotProduct(norm_vec, r.d);
        if (down == 0) return null;
        double t = up/down;
        if (t < 0 ) return  null;
        return new Hit(t, norm_vec, r.pointAt(t), color);
    }
}
