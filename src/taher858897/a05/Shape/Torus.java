package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import org.omg.CORBA.MARSHAL;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.*;
import static java.lang.Math.*;

public class Torus implements Shape {
    final Vec3 position;
    final double radiusCircle;
    final double radiusTorus;
    final Material material;

    final Vec3 minPos;
    final Vec3 maxPos;

    Group g;

    public Torus(Vec3 position, double radiusTorus, double radiusCircle, Material material) {
        this.position = position;
        this.radiusTorus = radiusTorus;
        this.radiusCircle = radiusCircle;

        this.material = material;

        minPos = Plane.MIN_POS;
        maxPos = Plane.MAX_POS;
        g = new Group(new Cylinder(vec3(0), radiusCircle, 2*PI*radiusTorus, material));
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

    @Override
    public Vec3 getMinPos() {
        return minPos;
    }

    @Override
    public Vec3 getMaxPos() {
        return maxPos;
    }

    public Hit intersect(Ray r, boolean withNormVec) {
        g.setTransformation(
            Mat4.rotate(
                vec3(0, 0,1),toDegrees(2*PI*r.d.y)
            )
        );

        return g.intersect(r);
    }
}
