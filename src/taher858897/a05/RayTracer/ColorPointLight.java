package taher858897.a05.RayTracer;

import cgtools.Vec3;

import static cgtools.Vec3.*;
import static java.lang.Math.pow;
import static taher858897.a05.Shape.Shape.EPSILON;

public class ColorPointLight implements Light {
    final Vec3 postition;
    final Vec3 emission;
    final Vec3 color;

    public ColorPointLight(Vec3 direction, Vec3 emisson, Vec3 color) {
        this.postition = direction;
        this.emission = emisson;
        this.color = color;
    }

    @Override
    public Sample sample(World world, Hit hitPoint, boolean intersect) {
        if (intersect){
            Hit firstHit = world.scene.intersect(new Ray(postition, normalizeFast(subtract(hitPoint.position, postition)), 0, Double.POSITIVE_INFINITY));
            if (subtract(hitPoint.position, firstHit.position).length() > EPSILON*2) return new Sample(postition, black);
        }
        Vec3 emission = divide(this.emission, pow(subtract(hitPoint.position, postition).length(),2));
        Vec3 L = multiply(emission, multiply(normalizeFast(subtract(hitPoint.position, postition)), hitPoint.normVec));

        return new Sample(postition, L, color);
    }
}
