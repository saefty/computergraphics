package taher858897.a05.Material;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public interface Material {
    Vec3 emittedRadiance(Ray r, Hit h);
    Ray scatteredRay(Ray r, Hit h);
    Vec3 albedo(Ray r, Hit h);
}
