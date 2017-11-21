package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.Material.ReflectionMaterial;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.blue;
import static cgtools.Vec3.green;
import static cgtools.Vec3.red;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Cube implements Shape {
    final Vec3 position;
    final Vec3 position_max;
    final Material material;

    public Cube(double size, Vec3 position, Material m) {
        this.position = position;
        this.position_max = new Vec3(position.x + size, position.y+size, position.z+size);
        this.material = m;
    }

    public Cube(Vec3 min, Vec3 max, Material m){
        this.position = min;
        this.position_max = max;
        this.material = m;
    }
    @Override
    public Hit intersect(Ray r) {
        double X_MIN = position.x;
        double X_MAX = position_max.x;
        double Y_MIN = position.y;
        double Y_MAX = position_max.y;
        double Z_MIN = position.z;
        double Z_MAX = position_max.z;

        double x_frac = 1.0f / r.d.x,
            y_frac = 1.0f / r.d.y,
            z_frac = 1.0f / r.d.z;
        double t1 = (X_MIN - r.o.x)*x_frac;
        double t2 = (X_MAX - r.o.x)*x_frac;
        double t3 = (Y_MIN - r.o.y)*y_frac;
        double t4 = (Y_MAX - r.o.y)*y_frac;
        double t5 = (Z_MIN - r.o.z)*z_frac;
        double t6 = (Z_MAX - r.o.z)*z_frac;

        double tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        double tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

        if (tmin > tmax || tmax < 0 ||tmin <= 0) {
            return null;
        }

        Vec3 norm_vec = new Vec3(0);
        if (tmin == t1) norm_vec = new Vec3(-1,0, 0);
        else if (tmin == t2) norm_vec = new Vec3(1,0, 0);
        else if (tmin == t3) norm_vec = new Vec3(0,-1, 0);
        else if (tmin == t4) norm_vec = new Vec3(0,1, 0);
        else if (tmin == t5) norm_vec = new Vec3(0,0, 1);
        else if (tmin == t6) norm_vec = new Vec3(0,0, -1);
        return new Hit(tmin, norm_vec, r.pointAt(tmin), material);
    }
}
