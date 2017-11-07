package taher858897.a05.Shape;

import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public interface Shape {
    Hit intersect(Ray r);
}
