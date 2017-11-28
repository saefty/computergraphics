package taher858897.a05;

import cgtools.Vec3;
import taher858897.Image;
import taher858897.a05.Camera.Camera;
import taher858897.a05.Camera.PanoramaCamera;
import taher858897.a05.Camera.StationaryCamera;
import taher858897.a05.Material.BackgroundMaterial;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.GlassMaterial;
import taher858897.a05.Material.ReflectionMaterial;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;
import taher858897.a05.Shape.*;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.GammaSampler;
import taher858897.a05.Shape.Shape;

import java.io.IOException;
import java.security.SecureRandom;

import static cgtools.Vec3.*;

public class Main {
    public static int width  = 160 * 12;
    public static int height = 90 * 12;

    private static final int SAMPLING_RATE = 128;
    private static final double GAMMA = 2.2;


    public static Sampler raytrace(Group scene, Camera camera){
        RayTracer raySampler = new RayTracer(scene, camera);
        return raySampler;
    }

    public static void main(String[] args) {
        long start_time = System.currentTimeMillis();
        Image image = new Image(width, height);

        Camera stationaryCamera = new StationaryCamera(Math.PI/2, width, height);
        Background bg = new Background(new BackgroundMaterial( new Vec3(.75)));

        Group scene = new Group(
            bg,
            //genRubics()
            genSnowmanScene()
        );
        Sampler tracer = raytrace(scene, stationaryCamera);

        renderImage(image, tracer);

        System.out.println((start_time-System.currentTimeMillis())/1000.0 + "s");
    }

    public static void renderImage(Image image, Sampler tracer){
        SampleMultithread sampleMultithread = new SampleMultithread(image, tracer, (SAMPLING_RATE*4)/8,8);
        try {

            ImageMultithreadSocketWriter writer = new ImageMultithreadSocketWriter("saeftaher.de", 8770, image, tracer, (SAMPLING_RATE*4)/8);
            Thread socketThread = new Thread(writer);

            socketThread.start();
            sampleMultithread.startMultiThreading();

            socketThread.join();
            sampleMultithread.join();
            image = sampleMultithread.getImages();
            image = Image.mergeAVG(writer.RESULT_IMG, image);
        } catch ( Exception e) {
            e.printStackTrace();
        }

        image.sample(
            new GammaSampler(image, GAMMA)
        );

        String filename = "doc/a05-diffuse-spheres.png";
        try {
            System.out.println("Start writing image: " + filename);

            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

    public static Vec3 middle(Vec3 a, Vec3 b){
        return new Vec3((a.x+b.x)/2, (a.y+b.y)/2, (a.z+b.z)/2);
    }

    public static Group sirpinski(Group shapes, Vec3 le_F, Vec3 re_F, Vec3 back, Vec3 top, int level){
        if (level==0) {
            return shapes;
        }

        level -= 1;

        // In das übergebene Dreieck, wird ein auf dem Kopf stehendes Dreieck eingefügt
        Vec3 center = middle(middle(le_F, re_F), back);
        Vec3 le_center = middle(le_F, top);
        Vec3 re_center = middle(re_F, top);
        Vec3 back_center = middle(back, top);


        shapes.addShapes(getTriangleShape(le_center, re_center, back_center, center, level).getShapes());

        // 3 neue Dreiecke bestimmen
        Vec3 a = le_F;
        Vec3 b = le_center;
        Vec3 c = middle(le_F, back);
        Vec3 d = middle(le_F, re_F);
        sirpinski(shapes, a, d, c, b, level);

        //sirpinski(shapes, le_center,re_center, back_center, center, level);

        a = middle(le_F, re_F);
        b = re_center;
        c = middle(back, re_F);
        d = re_F;
        sirpinski(shapes, a, d, c, b, level);


        a = middle(back, le_F);
        b = back_center;
        c = back;
        d = middle(back, re_F);
        sirpinski(shapes, a, d, c, b, level);

        a = re_center;
        b = top;
        c = back_center;
        d = le_center;
        sirpinski(shapes, a, d, c, b, level);
        return shapes;
    }

    private static Group getTriangleShape(Vec3 le_center, Vec3 newX, Vec3 newY, Vec3 newZ, int level) {
        SecureRandom sr = new SecureRandom();
        Vec3 rndColor = new Vec3(sr.nextDouble(), sr.nextDouble(), sr.nextDouble());
        rndColor = Vec3.normalize(rndColor);
        double size = .04;
        return new Group(
                new Sphere(le_center, size, new ReflectionMaterial(rndColor,.0)),
                new Sphere(newX, size, new ReflectionMaterial(rndColor,.0)),
                new Sphere(newY, size, new ReflectionMaterial(rndColor,.0)),
                new Sphere(newZ ,size, new ReflectionMaterial(rndColor,.0))
        );
    }

    public static Group genSirpinskiScene(){
        Background bg = new Background(new BackgroundMaterial(new Vec3(.8)));

        Shape ground = new Plane(vec3(0.0, -.3, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new Vec3(0.7)));

        Group scene = new Group(
                bg,
                ground,
                getTriangleShape(new Vec3(-.25,-.25,-.5), new Vec3(.25,-.25,-.5), new Vec3(0,-.25,-1.0), new Vec3(0, .25,-.5),3)
        );
        scene.addShape(sirpinski(
            new Group(),
            new Vec3(-.25,-.25,-.5), new Vec3(.25,-.25,-.5), new Vec3(0,-.25,-1.0), new Vec3(0, .25,-.5),
            2
        ));
        return scene;
    }

    public static Group genSnowmanScene(){
        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(vec3(.5)));

        Shape globe1 = new Sphere(vec3(-2.5, -.2, -1.5), 0.5, new DiffuseMaterial(vec3(.8)));
        Shape globe1_2 = new Sphere(vec3(-2.5, -.5, -1.5), 0.3,new DiffuseMaterial(vec3(.4)));
        Shape globe2 = new Sphere(vec3(-2.0, -.2, -3), 0.8,  new DiffuseMaterial(vec3(.8)));
        Shape globe3 = new Sphere(vec3(-2.5, 0, -6.5), 0.8, new DiffuseMaterial(red));

        Shape globe4 = new Sphere(vec3(1.5, -.5, -1.5), 0.4, new DiffuseMaterial(green));
        Shape globe5 = new Sphere(vec3(2.0, 0, -3), 0.8, new DiffuseMaterial(red));
        Shape globe6 = new Sphere(vec3(2.5, 0, -6.5), 0.8, new DiffuseMaterial(green));

        Shape le_eye = new Sphere(vec3(-0.065, 0.55 -  .2, -2.34+.5), .043 ,new DiffuseMaterial(black));
        Shape re_eye = new Sphere(vec3(0.065, 0.55  -  .2, -2.34+.5), .043 ,new DiffuseMaterial(black));
        Shape globe9 = new Sphere(vec3(0.0, 0.5     -  .2, -2.5+.5), .21, new DiffuseMaterial(vec3(.6)));
        Shape globe8 = new Sphere(vec3(0.0, .1      -  .2, -2.5+.5), .33, new DiffuseMaterial(vec3(.8)));
        Shape globe10 = new Sphere(vec3(0.0, -.5    -  .2, -2.5+.5), .4 , new DiffuseMaterial(vec3(.8)));
        Group snowMan = new Group(re_eye,le_eye, globe8,globe9,globe10);

        le_eye = new Sphere(vec3(-0.065 -.6, 0.55 -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        re_eye = new Sphere(vec3(0.065-.6, 0.55  -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        globe9 = new Sphere(vec3(0.0-.6, 0.5     -  .2, -2.5), .21 ,new DiffuseMaterial(vec3(.8)));
        globe8 = new Sphere(vec3(0.0-.6, .1      -  .2, -2.5), .33, new DiffuseMaterial(vec3(.8)));
        globe10 = new Sphere(vec3(0.0-.6, -.5    -  .2, -2.5), .4 , new DiffuseMaterial(vec3(.8)));
        Group snowMan1 = new Group(re_eye,le_eye, globe8,globe9,globe10);

        le_eye = new Sphere(vec3(-0.065 +.6, 0.55 -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        re_eye = new Sphere(vec3(0.065+.6, 0.55  -  .2, -2.34), .043 ,new DiffuseMaterial(black));
        globe9 = new Sphere(vec3(0.0+.6, 0.5     -  .2, -2.5), .21 ,new DiffuseMaterial(vec3(.8)));
        globe8 = new Sphere(vec3(0.0+.6, .1      -  .2, -2.5), .33, new DiffuseMaterial(vec3(.8)));
        globe10 = new Sphere(vec3(0.0+.6, -.5    -  .2, -2.5), .4 ,new DiffuseMaterial(vec3(.8)));
        Group snowMan2 = new Group(re_eye,le_eye, globe8,globe9,globe10);


        Group scene = new Group(ground, globe1, globe1_2, globe5 , globe2, globe3, globe4, globe6,
                snowMan1, snowMan2, snowMan,
                new Cube(vec3(-.1, -.7, -1.4), vec3(.1, -.5    +  .0, -2.1), new DiffuseMaterial(vec3(0,.5,1))),
                new Cube(vec3(-.1, -.7+.5, -1.4), vec3(.1, -.5    +  .5, -2.1), new DiffuseMaterial(vec3(0,.5,1))),
                new Cube(vec3(-1.9, -1, -2.9), vec3(-2.1, 1, -3.1), new DiffuseMaterial(blue)),
                new Cube(vec3(-.3, -1, -1.2), vec3(.3, -.5, -1.3),  new DiffuseMaterial(vec3(.8))),
                new Cube(vec3(-.3, -1, -0.8), vec3(.3, -.7, -0.9),  new DiffuseMaterial(vec3(.8)))
        );

        return scene;
    }

    public static Group genRubics(){
        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(vec3(.5)));

        Group cubeLayer1 = new Group(
                ground,
                new Cube(.2, vec3(-1, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1+1, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75+1, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45+1, -.5, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1+1, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75+1, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45+1, -.5, -1.75-.5), new DiffuseMaterial(rndColor())),

                new Cube(.2, vec3(-1, -.5, -1.5+.7-.5), new ReflectionMaterial(rndColor(),.5)),
                new Cube(.2, vec3(-.75, -.5, -1.5+.7-.5), new ReflectionMaterial(rndColor(),.4)),
                new Cube(.2, vec3(-.45, -.5, -1.5+.7-.5), new ReflectionMaterial(rndColor(),.3)),
                new Cube(.2, vec3(-1, -.5, -1.75+.7-.5), new ReflectionMaterial(rndColor(),.2)),
                new Cube(.2, vec3(-.75, -.5, -1.75+.7-.5), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.45, -.5, -1.75+.7-.5), new ReflectionMaterial(rndColor(),.0))
        );

        Group cubeLayer2 = new Group(
                new Cube(.2, vec3(-1, -.25, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.25, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.25, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1, -.25, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.25, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.25, -1.75-.5), new DiffuseMaterial(rndColor()))

        );

        Group cubeLayer3 = new Group(
                new Cube(.2, vec3(-1, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),

                new Cube(.2, vec3(-1, -.75, -1.5+.7-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.75, -1.5+.7-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.75, -1.5+.7-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1, -.75, -1.75+.7-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75, -.75, -1.75+.7-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45, -.75, -1.75+.7-.5), new DiffuseMaterial(rndColor())),

                new Cube(.2, vec3(-1+1, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75+1, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45+1, -.75, -1.5-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-1+1, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.75+1, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),
                new Cube(.2, vec3(-.45+1, -.75, -1.75-.5), new DiffuseMaterial(rndColor())),

                new Cube(.2, vec3(-1+1, -.75, 1.5+.7-.5), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.75+1, -.75, 1.5+.7-.5), new ReflectionMaterial(rndColor(),.2)),
                new Cube(.2, vec3(-.45+1, -.75, 1.5+.7-.5), new ReflectionMaterial(rndColor(),.3)),
                new Cube(.2, vec3(-1+1, -.75, 1.75+.7-.5), new ReflectionMaterial(rndColor(),.0)),
                new Cube(.2, vec3(-.75+1, -.75, 1.75+.7-.5), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.45+1, -.75, 1.75+.7-.5), new ReflectionMaterial(rndColor(),.2))
        );

        return new Group(cubeLayer1, cubeLayer2, cubeLayer3);

    }

    public static Vec3 rndColor(){
        return new Vec3(Math.random(), Math.random(), Math.random());
    }



}

