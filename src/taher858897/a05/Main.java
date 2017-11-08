package taher858897.a05;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.Image;
import taher858897.a05.Camera.Camera;
import taher858897.a05.Camera.StationaryCamera;
import taher858897.a05.Material.BackgroundMaterial;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.Material.ReflectionMaterial;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;
import taher858897.a05.Shape.*;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.GammaSampler;

import java.io.IOException;

import static cgtools.Vec3.*;

public class Main {
    private static int width  = 160 * 6;
    private static int height = 90 * 6;

    private static final double GAMMA = 2.2;
    private static final int SAMPLING_RATE = 255;


    public static Sampler raytrace(Group scene, Camera camera, int sample_rate){
        RayTracer raySampler = new RayTracer(scene, camera);
        StratifiedSampler s = new StratifiedSampler(raySampler, sample_rate);
        return new GammaSampler(s, GAMMA);
    }

    public static void main(String[] args) {
        Image image = new Image(width, height);

        StationaryCamera stationaryCamera = new StationaryCamera(Math.PI/2, width, height);
        Background bg = new Background(new BackgroundMaterial(new Vec3(.8)));

        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new Vec3(0.5)));


        Shape globe1 = new Sphere(vec3(-2.5, 0, -1.5), 0.5, new ReflectionMaterial(red,.2));
        Shape globe2 = new Sphere(vec3(-2.0, 0, -3), 0.8, new ReflectionMaterial(new Vec3(0.8),.4));
        Shape globe3 = new Sphere(vec3(-2.5, 0, -6.5), 0.8, new ReflectionMaterial(red, .8));

        Shape globe4 = new Sphere(vec3(2.5, 0, -1.5), 0.5, new DiffuseMaterial(green));
        Shape globe5 = new Sphere(vec3(2.0, 0, -3), 0.8, new ReflectionMaterial(red,0));
        Shape globe6 = new Sphere(vec3(2.5, 0, -6.5), 0.8,new DiffuseMaterial(green));

        Shape le_eye = new Sphere(vec3(-0.065, 0.55 -  .2, -2.34+.5), .043 ,new DiffuseMaterial(black));
        Shape re_eye = new Sphere(vec3(0.065, 0.55  -  .2, -2.34+.5), .043 ,new DiffuseMaterial(black));
        Shape globe9 = new Sphere(vec3(0.0, 0.5     -  .2, -2.5+.5), .21 ,new DiffuseMaterial(vec3(.8)));
        Shape globe8 = new Sphere(vec3(0.0, .1      -  .2, -2.5+.5), .33, new DiffuseMaterial(vec3(.8)));
        Shape globe10 = new Sphere(vec3(0.0, -.5    -  .2, -2.5+.5), .4 ,new ReflectionMaterial(vec3(.8),.0));
        Group snowMan = new Group(re_eye,le_eye, globe8,globe9,globe10);

        le_eye = new Sphere(vec3(-0.065 -.6, 0.55 -  .2, -2.34), .043 ,new DiffuseMaterial(black));
         re_eye = new Sphere(vec3(0.065-.6, 0.55  -  .2, -2.34), .043 ,new DiffuseMaterial(black));
         globe9 = new Sphere(vec3(0.0-.6, 0.5     -  .2, -2.5), .21 ,new DiffuseMaterial(vec3(.8)));
         globe8 = new Sphere(vec3(0.0-.6, .1      -  .2, -2.5), .33, new DiffuseMaterial(vec3(.8)));
         globe10 = new Sphere(vec3(0.0-.6, -.5    -  .2, -2.5), .4 ,new ReflectionMaterial(vec3(.8), .2));
        Group snowMan1 = new Group(re_eye,le_eye, globe8,globe9,globe10);

        le_eye = new Sphere(vec3(-0.065 +.6, 0.55 -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        re_eye = new Sphere(vec3(0.065+.6, 0.55  -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        globe9 = new Sphere(vec3(0.0+.6, 0.5     -  .2, -2.5), .21 ,new DiffuseMaterial(vec3(.8)));
        globe8 = new Sphere(vec3(0.0+.6, .1      -  .2, -2.5), .33, new DiffuseMaterial(vec3(.8)));
        globe10 = new Sphere(vec3(0.0+.6, -.5    -  .2, -2.5), .4 ,new ReflectionMaterial(vec3(.8),.6));
        Group snowMan2 = new Group(re_eye,le_eye, globe8,globe9,globe10);

        Group scene = new Group(bg, ground, globe1, globe5,globe1 , globe2, globe3, globe4, globe6, snowMan,snowMan1, snowMan2); //

        Sampler s = raytrace(scene, stationaryCamera, SAMPLING_RATE);

        ImageMultithread imageMultithread = new ImageMultithread(image, s);
        imageMultithread.startMultiThreading();

        //image.sample(s);


        String filename = "doc/a05-scene.png";
        try {
            System.out.println("Start writing image: " + filename);

            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

}
