package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.RayTracer.Ray;

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
        return Vec3.dotProduct(tmp, tmp) <= radius*radius;
    }

    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 tmp = Vec3.add(
                x,
                Vec3.multiply(
                        -1,
                        new Vec3(position.x, position.y, position.z)
                ));
        return Vec3.divide(tmp, radius);
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 tmpX0 = new Vec3(r.o.x - position.x, r.o.y - position.y,r.o.z - position.z);
        //if (Vec3.dotProduct(r.o, position) <= radius*radius) return null;

        double a = Vec3.dotProduct(r.d, r.d);
        double b = 2 * Vec3.dotProduct(tmpX0, r.d);
        double c = Vec3.dotProduct(tmpX0, tmpX0) - radius * radius;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;
        double d_sqrt = Math.sqrt(discriminant);

        double t1 = -(b  + d_sqrt)/(2*a);
        double t2 = -(b - d_sqrt)/(2*a);
        /*System.out.println("a " + a);
        System.out.println("b " + b);
        System.out.println("c " + c);
        System.out.println("t1 " + t1);
        System.out.println("t2 " + t2);
        System.out.println("discre " + discriminant);*/

        double result = -1;
        if (t1 >= 0 && discriminant == 0)
            result = t1;
        else if (t2 >= 0 && discriminant == 0)
            result = t2;
        else if (discriminant > 0)
            if (t1 > 0 && t2 > 0)
                result = Math.min(t1, t2);
            else if (t1 > 0)
                result = t1;
            else if (t2 > 0)
                result = t2;

        if (r.t0 > result || r.t1 < result) return null;
        Vec3 hitPoint = r.pointAt(result);

        return new Hit(
                result,
                getNormVecAtPoint(
                        hitPoint
                ),
                hitPoint,
                material);
    }
}
