package taher858897.a04.Camera;

import taher858897.a04.RayTracer.Ray;

public interface Camera {
    Ray generateRay(double x, double y);
}
