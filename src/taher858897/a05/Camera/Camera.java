package taher858897.a05.Camera;

import taher858897.a05.RayTracer.Ray;

public interface Camera {
    Ray generateRay(double x, double y);
}
