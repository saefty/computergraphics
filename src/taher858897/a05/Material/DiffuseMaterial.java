package taher858897.a05.Material;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

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
        Vec3 rndD = rndDirection();
        rndD = Vec3.normalize(rndD);
        rndD = Vec3.add(h.normVec, rndD);
        return new Ray(h.position, rndD, EPSILON, Double.POSITIVE_INFINITY);
    }

    public Vec3 rndDirection(){
        Vec3 rndD = new Vec3(Random.random(), Random.random(), Random.random());
        while (rndD.x * rndD.x + rndD.y * rndD.y + rndD.z * rndD.z >= 1){
            rndD = new Vec3(Random.random(), Random.random(), Random.random());
        }
        return rndD;
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
