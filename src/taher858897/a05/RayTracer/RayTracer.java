package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.Camera.Camera;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Shape.Group;

public class RayTracer implements Sampler {
    final Group scene;
    final Camera camera;

    public RayTracer(Group scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
    }


    @Override
    public Vec3 color(double x, double y) {
        Ray r = camera.generateRay(x, y);
        return radiance(r,6);
    }


    private Vec3 radiance(Ray r, int depth) {
        if (depth == 0) return Vec3.black;

        Hit newHit = scene.intersect(r);

        Vec3 emittedRadiance = newHit.material.emittedRadiance(r, newHit);
        Ray scatteredRay = newHit.material.scatteredRay(r, newHit);

        if (scatteredRay != null){
            return Vec3.add(emittedRadiance, Vec3.multiply(newHit.material.albedo(r, newHit), radiance(scatteredRay, depth - 1)));
        }
        else
            return emittedRadiance;
    }
}
