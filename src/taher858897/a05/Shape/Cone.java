package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.GlassMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.*;
import static java.lang.Math.*;

public class Cone implements Shape {
    final Vec3 position;
    final double radius;
    final double radians;
    final double height;
    final Material material;
    final Disk topDisk;

    public Cone(Vec3 position, double height, double deg, Material material) {
        this.position = position;
        this.height = height;
        this.radians = Math.toRadians(deg);

        this.radius = tan(this.radians) * height;
        this.material = material;
        this.topDisk = new Disk(vec3(position.x,position.y + height, position.z), vec3(0,1,0), material, radius*height);
    }


    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 tmp = subtract(x, position);
        tmp = vec3(tmp.x, 0, tmp.z);
        return tmp;
    }

    @Override
    public Hit intersect(Ray r) {
        return intersect(r, true);
    }

    public Hit intersect(Ray r, boolean withNormVec) {
        Vec3 x0 = new Vec3(r.o.x - position.x, (r.o.y - position.y),r.o.z - position.z);
        Vec3 d = r.d;

        double a = d.x * d.x + d.z * d.z - (d.y*d.y)*radius*radius;
        double b = 2*x0.x*d.x + 2*x0.z*d.z - (2*x0.y*d.y)*radius*radius;
        double c = x0.x*x0.x + x0.z*x0.z - (x0.y*x0.y)*radius*radius;
        /*Vec3 tmpX0 = new Vec3(r.o.x - position.x, radius*(r.o.y - position.y - height),r.o.z - position.z);
        Vec3 tmpX0_mod = new Vec3(r.o.x - position.x, -radius*(r.o.y - position.y),r.o.z - position.z);
        Vec3 direction = new Vec3(r.d.x, -radius*r.d.y, r.d.z);
        Vec3 direction_mod = new Vec3(r.d.x, radius*r.d.y, r.d.z);

        double a = dotProduct(direction, direction_mod);
        double b = dotProduct(tmpX0, direction) + dotProduct(tmpX0_mod, direction_mod);
        double c = dotProduct(tmpX0_mod, tmpX0);*/

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

        Hit h = new Hit(result, normalizeFast(vec3(cos(radians), sin(radians), -hitPoint.z)), hitPoint, material);
        //h= null;
        if (hitPoint.y < position.y) {
            return null;
        }
        else if (subtract(vec3(position.x, hitPoint.y, position.z), position).length() > height )
            return topDisk.intersect(r);

        return h;
    }
}
