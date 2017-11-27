package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.dotProduct;
import static cgtools.Vec3.multiply;
import static cgtools.Vec3.multiplyFast;
import static java.lang.Math.signum;

public class Sphere implements Shape {
    final Vec3 position;
    final double radius;
    final Material material;

    public Sphere(Vec3 position, double radius,Material material) {
        this.position = position;
        this.radius = radius;
        this.material = material;
    }

    public boolean contains(Vec3 x) {
        Vec3 tmp = Vec3.add(
                x,
                Vec3.multiply(
                        -1,
                        new Vec3(position.x, position.y, position.z)
                ));
        return dotProduct(tmp, tmp) <= radius*radius;
    }

    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 tmp = Vec3.subtract(
                x,
                position
                );
        return Vec3.divide(tmp, radius);
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 tmpX0 = new Vec3(r.o.x - position.x, r.o.y - position.y,r.o.z - position.z);
        //if (Vec3.dotProduct(r.o, position) <= radius*radius) return null;

        double a = dotProduct(r.d, r.d);
        double b = 2 * dotProduct(tmpX0, r.d);
        double c = dotProduct(tmpX0, tmpX0) - radius * radius;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;
        double d_sqrt = Math.sqrt(discriminant);

        double t1 = -(b  + d_sqrt)/(2*a);
        double t2 = -(b - d_sqrt)/(2*a);

        double result = -1;
        double t = Math.min(t1, t2);
        if (t <= 0) t = Math.max(t1, t2);
        result = t;
        /*if (t1 >= 0 && discriminant == 0)
            result = t1;
        else if (t2 >= 0 && discriminant == 0)
            result = t2;
        else if (discriminant > 0)
            if (t1 > 0 && t2 > 0)
                result = Math.min(t1, t2);
            else if (t1 > 0)
                result = t1;
            else if (t2 > 0)
                result = t2;*/

        if (r.t0 > result || r.t1 < result) return null;
        Vec3 hitPoint = r.pointAt(result);
        Vec3 norm_vec = getNormVecAtPoint(hitPoint);
        if (dotProduct(r.d, norm_vec) > 0){
            norm_vec = multiplyFast(-1, norm_vec);
        }
        return new Hit(
                result,
                norm_vec,
                hitPoint,
                material);
    }
}
