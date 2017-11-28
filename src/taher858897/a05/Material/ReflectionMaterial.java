package taher858897.a05.Material;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.dotProduct;
import static cgtools.Vec3.normalize;
import static taher858897.a05.Shape.Shape.EPSILON;

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
        Vec3 reflectedDirection = normalize(reflectedDirection(r, h));
        return new Ray(h.position, reflectedDirection, EPSILON, Double.POSITIVE_INFINITY);
}

    public Vec3 reflectedDirection(Ray r, Hit h){
        Vec3 refl = Vec3.multiply(-2 * dotProduct(h.normVec, r.d), h.normVec);
        return Vec3.addFast(refl, r.d, Vec3.multiplyFast(rnd_factor, Vec3.randomDirection()));
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
