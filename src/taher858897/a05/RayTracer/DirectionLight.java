package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.Material.BackgroundMaterial;
import taher858897.a05.Shape.Background;

import static cgtools.Vec3.*;
import static java.lang.Math.pow;
import static taher858897.a05.Shape.Shape.EPSILON;

public class DirectionLight implements Light {
    final Vec3 direction;
    final Vec3 emission;

    public DirectionLight(Vec3 direction, Vec3 emisson) {
        this.direction = direction;
        this.emission = emisson;
    }

    @Override
    public Sample sample(World world, Hit hitPoint, boolean intersect) {
        if (intersect){
            Hit h = world.scene.intersect(
                new Ray(hitPoint.position, multiply(-1,direction),EPSILON, Double.POSITIVE_INFINITY)
            );
            if (h != null && !(h.material instanceof BackgroundMaterial))
                return new Sample(direction, black);
        }
        return new Sample(direction, multiply(emission, multiply(direction, hitPoint.normVec)));
    }
}
