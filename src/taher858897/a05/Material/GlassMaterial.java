package taher858897.a05.Material;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import static cgtools.Vec3.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static taher858897.a05.Shape.Shape.EPSILON;

public class GlassMaterial extends ReflectionMaterial implements Material{
    private double WORLD_REFRACTION_INDEX = 1.000272;
    private Vec3 color;
    private double refractionIndex;

    public GlassMaterial(Vec3 color, double refractionIndex, double rnd_factor) {
        super(color, rnd_factor);
        this.color = color;
        this.refractionIndex = refractionIndex;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return Vec3.black;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        double n1 = WORLD_REFRACTION_INDEX;
        double n2 = refractionIndex;
        if (Vec3.dotProduct(r.d, h.normVec) > 0 ){
            h = new Hit(h.t, Vec3.multiply(-1, h.normVec), h.position, h.material);
            double tmp = n1;
            n1 = n2;
            n2 = tmp;
        }

        Ray scattered;
        Ray refracted = refract(r, h, n1, n2);
        if (refracted != null) {
            if (Random.random()  > schlick(r, h, n1, n2)){
                scattered = refracted;
            }
            else {
                scattered = super.scatteredRay(r, h);
            }
        }
        else {
            scattered = super.scatteredRay(r, h);
        }
        return scattered;
    }

    public double schlick(Ray r, Hit h, double n1, double n2){
        double r0 = pow((n1-n2)/(n2+n1), 2);
        return r0 + (1 - r0)* pow(1 + Vec3.dotProduct(h.normVec, r.d), 5);
    }

    public Ray refract(Ray r, Hit h, double n1, double n2) {
        Vec3 dir = refractDirection(r, h, n1, n2);
        if (dir == null) return super.scatteredRay(r, h);
        return new Ray(h.position, normalizeFast(dir), EPSILON, Double.POSITIVE_INFINITY);
    }

    public Vec3 refractDirection(Ray ray, Hit h, double n1, double n2) {
        double r = n1/n2;
        double c = - Vec3.dotProduct(h.normVec, ray.d);
        double discremninant = 1 - pow(r, 2) * (1 - pow(c, 2));
        /*
         * Bei negativer Diskriminante 1−r*r(1−c2)<0
         */
        if (discremninant < 0) return null;
        return (addFast(multiply(r, ray.d), multiply((r*c-sqrt(discremninant)), h.normVec)));
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return color;
    }
}
