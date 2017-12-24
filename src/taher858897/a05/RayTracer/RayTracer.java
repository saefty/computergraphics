package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.Camera.Camera;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Shape.Group;

import java.util.ArrayList;

import static cgtools.Vec3.*;
import static java.lang.Math.*;

public class RayTracer implements Sampler {
    final Camera camera;
    final World world;

    public static final int DEPTH = 8;

    public RayTracer(Group scene, Camera camera) {
        this.world = new World(scene, new ArrayList<Light>());
        this.camera = camera;
    }

    public RayTracer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }


    @Override
    public Vec3 color(double x, double y) {
        Ray r = camera.generateRay(x, y);
        return radiance(r,DEPTH);
    }

    private Vec3 radiance(Ray r, int depth) {
        if (depth == 0) return Vec3.black;

        Hit newHit = world.scene.intersect(r);
        ArrayList<Light.Sample> lightSamples = new ArrayList<>();

        for (Light light : world.lights) {
             lightSamples.add(light.sample(world, newHit, true));
        }

        Vec3 emittedRadiance = newHit.material.emittedRadiance(r, newHit);
        Ray scatteredRay = newHit.material.scatteredRay(r, newHit);
        Vec3 radiance;
        if (scatteredRay != null){
            Vec3 recColor = Vec3.multiply(newHit.material.albedo(r, newHit), radiance(scatteredRay, depth - 1));
            radiance = add(emittedRadiance, recColor);

            for (Light.Sample s: lightSamples) {
                double Lr = dotProduct(newHit.material.albedo(r, newHit), s.emission);
                radiance = addFast(vec3(abs(Lr)), radiance);
            }
            // foggy
            //if (newHit.t > .5){
            //    radiance = addFast(radiance, vec3((min(newHit.t, 30))/2 *.02));
            //}

        }
        else{
            radiance = emittedRadiance;
        }

        return radiance;

    }
}
