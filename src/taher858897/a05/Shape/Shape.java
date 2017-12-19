package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.io.Serializable;

public interface Shape extends Serializable {
    static double EPSILON = 0.000000000001;
    Hit intersect(Ray r);
    Vec3 getMinPos();
    Vec3 getMaxPos();

}
