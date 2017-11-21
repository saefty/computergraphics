package taher858897.a05.Camera;

import taher858897.a05.RayTracer.Ray;

import java.io.Serializable;

public interface Camera extends Serializable {
    Ray generateRay(double x, double y);
}
