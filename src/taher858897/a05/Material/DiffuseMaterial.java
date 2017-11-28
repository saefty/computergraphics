package taher858897.a05.Material;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.normalizeFast;
import static taher858897.a05.Shape.Shape.EPSILON;

public class DiffuseMaterial implements Material{
    final Vec3 color;

    public DiffuseMaterial(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return Vec3.black;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        Vec3 rndD = Vec3.randomDirection();
        rndD = Vec3.add(h.normVec, rndD);
        return new Ray(h.position, normalizeFast(rndD), EPSILON, Double.POSITIVE_INFINITY);
    }


    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
