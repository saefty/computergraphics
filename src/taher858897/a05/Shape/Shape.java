package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.io.IOException;
import java.io.Serializable;

public interface Shape extends Serializable {
    double EPSILON = 0.000000000001;
    Hit intersect(Ray r);
    BoundingBox bounds();
    boolean contains(Vec3 pos);

    void loadTextures() throws IOException;
}
