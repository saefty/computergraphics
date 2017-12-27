package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.signum;

public class Sphere implements Shape {
    final Vec3 position;
    final double radius;
    final Material material;

    final Vec3 minPos;
    final Vec3 maxPos;

    public Sphere(Vec3 position, double radius,Material material) {
        this.position = position;
        this.radius = radius;
        this.material = material;
        maxPos = new Vec3(position.x+radius,position.y+radius,position.z+radius);
        minPos = new Vec3(position.x-radius,position.y-radius,position.z-radius);
    }

    public boolean contains(Vec3 x) {
        Vec3 tmp = subtract(position, x);
        return dotProduct(tmp, tmp) < radius*radius;
    }

    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 tmp = Vec3.subtract(
                x,
                position
                );
        return Vec3.divideFast(tmp, radius);
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

        double result;
        double t = Math.min(t1, t2);
        if (t <= 0) t = Math.max(t1, t2);
        result = t;

        if (r.t0 > result || r.t1 < result) return null;
        Vec3 hitPoint = r.pointAt(result);
        Vec3 normal = getNormVecAtPoint(hitPoint);
        normal = normalizeFast(normal);

        double inclination = Math.acos(normal.y);
        double azimuth = Math.PI + Math.atan2(normal.x, normal.z);
        double u = azimuth / (2 * Math.PI);
        double v = inclination / Math.PI;

        return new Hit(
                result,
                normal,
                hitPoint,
                new Vec3(u,v,0),
                material);
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox bb = new BoundingBox(vec3(0),vec3(0));
        bb = bb.extend(minPos);
        bb = bb.extend(maxPos);
        return bb;
    }
}
