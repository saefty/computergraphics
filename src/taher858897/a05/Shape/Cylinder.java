package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.*;

public class Cylinder implements Shape {
    final Vec3 position;
    final double radius;
    final double height;
    final Material material;
    final Disk topDisk;
    final Disk bottomDisk;

    public Cylinder(Vec3 position, double radius, double height, Material material) {
        this.position = position;
        this.radius = radius;
        this.height = height;
        this.material = material;
        this.topDisk = new Disk(vec3(position.x,position.y+height, position.z), vec3(0,1,0), material, radius);
        this.bottomDisk = new Disk(vec3(position.x,position.y, position.z), vec3(0,-1,0), material, radius);
    }


    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 tmp = subtract(x, position);
        tmp = vec3(tmp.x, 0, tmp.z);
        return normalizeFast(tmp);
    }

    @Override
    public Hit intersect(Ray r) {
            Vec3 tmpX0 = new Vec3(r.o.x - position.x, 0,r.o.z - position.z);
            Vec3 direction = new Vec3(r.d.x, 0, r.d.z);


            double a = dotProduct(direction, direction);
            double b = 2 * dotProduct(tmpX0, direction);
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
            Vec3 norm_vec = getNormVecAtPoint(hitPoint);
            Hit h = new Hit(result, norm_vec, hitPoint, material);

            if (hitPoint.y < position.y || hitPoint.y - position.y > height) {
                h = topDisk.intersect(r);
                if (h == null) h = bottomDisk.intersect(r);
            }


            return h;
    }
}
