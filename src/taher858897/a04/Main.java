package taher858897.a04;

import taher858897.Image;
import taher858897.a04.Camera.Camera;
import taher858897.a04.Camera.StationaryCamera;
import taher858897.a04.RayTracer.RayTracer;
import taher858897.a04.Sampler.GammaSampler;
import taher858897.a04.Sampler.Sampler;
import taher858897.a04.Sampler.StratifiedSampler;
import taher858897.a04.Shape.*;

import java.io.IOException;

import static cgtools.Vec3.*;

public class Main {
    private static int width  = 160 * 12;
    private static int height = 90 * 12;

    private static final double GAMMA = 2.2;
    private static final int SAMPLING_RATE = 2;


    public static Sampler raytrace(Group scene, Camera camera, int sample_rate){
        RayTracer raySampler = new RayTracer(scene, camera);
        StratifiedSampler s = new StratifiedSampler(raySampler, sample_rate);
        return new GammaSampler(s, GAMMA);
    }

    public static void main(String[] args) {
        Image image = new Image(width, height);

        StationaryCamera stationaryCamera = new StationaryCamera(Math.PI/2, width, height);
        Background bg = new Background(white);

        Shape ground = new Plane(vec3(2.0, 0.0, 0.0), vec3(1, 0, 0), vec3(.5,.5,.5));
        Shape ground2 = new Plane(vec3(-2, -0.5, 0.0), vec3(1, .3, -0.3), vec3(.9,.5,.0));
        Shape ground3 = new Plane(vec3(-2, -0.5, 0.0), vec3(0, 1, -0.0), vec3(.1,.8,.25));


        Shape globe1 = new Sphere(vec3(-2.0, -0.3, -1.5), 0.4, red);
        Shape globe2 = new Sphere(vec3(-2.0, -0.3, -3), 0.5, green);
        Shape globe3 = new Sphere(vec3(-2.0, -0.3, -6.5), 0.8, blue);

        Shape globe4 = new Sphere(vec3(2.0, -0.3, -1.5), 0.4, blue);
        Shape globe5 = new Sphere(vec3(2.0, -0.3, -3), 0.5, red);
        Shape globe6 = new Sphere(vec3(2.0, -0.3, -6.5), 0.8, green);
        Group scene = new Group(ground, globe1, globe2, globe3, bg, ground2, ground3, globe4, globe5, globe6);

        Sampler s = raytrace(scene, stationaryCamera, SAMPLING_RATE);

        image.sample(s);


        String filename = "doc/a04-scene.png";
        try {
            System.out.println("Start writing image: " + filename);

            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

}
