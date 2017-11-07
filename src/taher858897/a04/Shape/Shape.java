package taher858897.a04.Shape;

import taher858897.a04.RayTracer.Hit;
import taher858897.a04.RayTracer.Ray;

public interface Shape {
    Hit intersect(Ray r);
}
