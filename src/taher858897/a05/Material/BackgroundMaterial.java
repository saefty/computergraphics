package taher858897.a05.Material;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class BackgroundMaterial implements Material{
    final Vec3 color;

    public BackgroundMaterial(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return color;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        return null;
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
