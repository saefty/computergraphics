package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;
import taher858897.a05.RootFinder;

import static cgtools.Vec3.*;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isNaN;
import static java.lang.Math.*;

public class Torus implements Shape {
    final Vec3 position;
    final double radiusCenter;
    final double radiusCylinder;
    final Material material;

    final Vec3 minPos;
    final Vec3 maxPos;

    public Torus(Vec3 position, double radiusCenter, double radiusCylinder, Material material) {
        this.position = position;
        this.radiusCenter = radiusCenter;
        this.radiusCylinder = radiusCylinder;
        this.material = material;


        //minPos = new Vec3(position.x-radius, position.y-EPSILON, position.z+radius);
        //maxPos = new Vec3(position.x+radius, position.y+height, position.z-radius);
        minPos = Plane.MIN_POS;
        maxPos = Plane.MAX_POS;
    }


    public Vec3 getNormVecAtPoint(Vec3 x){
        Vec3 xyHit = vec3(x.x, 0, x.z);
        double l = xyHit.length();
        Vec3 p = divideFast(xyHit,l);
        Vec3 n = subtract(multiply(radiusCenter, x), p);
        return normalizeFast(n);
    }

    @Override
    public Hit intersect(Ray r) {
        Vec3 d = r.d;
        Vec3 o = new Vec3(r.o.x - position.x, r.o.y - position.y,r.o.z - position.z);;
        double b0 = dotProduct(o, o) + pow(radiusCenter, 2) - pow(radiusCylinder,2);
        double b1 = 2* dotProduct(d, o);
        double b2 = dotProduct(d, d);

        Vec3 Q0 = vec3(o.x,0, o.z);//UNCLEAR
        Vec3 Qd = vec3(d.x, 0,d.z);//UNCLEAR

        double c0 =4*pow(radiusCenter,2)* dotProduct(Q0,Q0), c1 = 8*pow(radiusCenter,2)*dotProduct(Q0,Qd);
        double c2 = 4*pow(radiusCenter,2)*dotProduct(Qd,Qd);

        double a0 = pow(b0, 2) - c0;
        double a1 = 2*b0*b1 - c1;
        double a2 = (pow(b1,2)+ 2*b2*b0) - c2;
        double a3 = 2*b1*b2;
        double a4 = pow(b2, 2);

        double rootsTmp[] = {a0, a1, a2, a3, a4};
        double[] roots = RootFinder.SolveQuartic(rootsTmp);
        if (roots == null || roots.length == 0)  return null;

        double min = Double.POSITIVE_INFINITY;
        for (double root : roots) {
            if (root < min && root > 0) {
                min = root;
            }
        }
        if (min == Double.POSITIVE_INFINITY) return null;
        Vec3 hitPoint = r.pointAt(min);

        return new Hit(
                min,
                getNormVecAtPoint(hitPoint),
                hitPoint,
                new Vec3(0,0,0),
                material);
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox bb = new BoundingBox(vec3(0),vec3(0));
        bb = bb.extend(minPos);
        bb = bb.extend(maxPos);
        return bb;
    }

    @Override
    public boolean contains(Vec3 pos) {
        return false;
    }
}
