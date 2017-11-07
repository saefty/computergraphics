package taher858897.a04.RayTracer;

import cgtools.Vec3;
import taher858897.a04.Camera.Camera;
import taher858897.a04.Sampler.Sampler;
import taher858897.a04.Shape.Group;

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
        Hit h = scene.intersect(r);

        return h.color;
    }
}
