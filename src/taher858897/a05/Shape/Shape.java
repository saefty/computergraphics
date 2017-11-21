package taher858897.a05.Shape;

import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.io.Serializable;

public interface Shape extends Serializable {
    static double EPSILON = 0.00000000000001;
    Hit intersect(Ray r);

}
