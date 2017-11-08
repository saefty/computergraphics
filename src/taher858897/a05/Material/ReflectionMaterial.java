package taher858897.a05.Material;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class ReflectionMaterial implements Material{
    final Vec3 color;
    final double rnd_factor;

    public ReflectionMaterial(Vec3 color, double rnd_factor) {
        this.color = color;
        this.rnd_factor = rnd_factor;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return Vec3.black;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        Vec3 reflectedDirection = reflectedDirection(r, h);
        return new Ray(h.position, reflectedDirection,0, Double.POSITIVE_INFINITY);
    }

    public Vec3 rndDirection(){
        Vec3 rndD = new Vec3(Random.random(), Random.random(), Random.random());
        while (rndD.x * rndD.x + rndD.y * rndD.y + rndD.z * rndD.z >= 1){
            rndD = new Vec3(Random.random(), Random.random(), Random.random());
        }
        return rndD;
    }

    public Vec3 reflectedDirection(Ray r, Hit h){
        Vec3 refl = Vec3.multiply(-2 * Vec3.dotProduct(h.normVec, r.d), h.normVec);
        return Vec3.add(r.d, refl, Vec3.multiply(rnd_factor, rndDirection()));
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
