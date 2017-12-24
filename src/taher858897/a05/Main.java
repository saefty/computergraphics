package taher858897.a05;

import cgtools.ImageTexture;
import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.FootballScene;
import taher858897.Image;
import taher858897.a05.Camera.Camera;
import taher858897.a05.Camera.PanoramaCamera;
import taher858897.a05.Camera.StationaryCamera;
import taher858897.a05.Material.*;
import taher858897.a05.RayTracer.*;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Shape.*;
import taher858897.a05.Sampler.GammaSampler;
import taher858897.a05.Shape.Shape;
import taher858897.a05.Textures.PicTexture;
import taher858897.a05.Textures.TransformedPicTexture;
import taher858897.a05.Threading.Executors.RayTraceExcecutor;
import taher858897.a05.Threading.Executors.RayTraceFragmentExcecutor;
import taher858897.a05.Threading.ImageMultithreadSocketWriter;
import taher858897.a05.Threading.SampleMultithread;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

import static cgtools.Mat4.scale;
import static cgtools.Vec3.*;
import static java.lang.Math.*;
import static taher858897.a05.Shape.Group.buildBVH;

public class Main {
    public static String  filename = "doc/a10-2.png";
    public static int width  = 160 * 6;
    public static int height = 90 * 6;
    public static int threads = 8;
    public static int xDim = 160;
    public static int yDim = 90;


    private static final int SAMPLING_RATE = 32;
    private static final double GAMMA = 2.2;

    private static final boolean WITH_SOCKET = false;


    public static Sampler raytrace(World world, Camera camera){
        RayTracer raySampler = new RayTracer(world, camera);
        return raySampler;
    }

    public static void main(String[] args) throws IOException {
        Image image = new Image(width, height);
        Mat4 transformation = Mat4.rotate(vec3(-1,0,0),45).multiply(Mat4.translate(vec3(0,.5,2)));
        //transformation = Mat4.translate(vec3(0,0,2));
        //transformation = Mat4.translate(vec3(0,.5,4.5)).multiply(Mat4.rotate(vec3(1,0,0),-10)); //2
        //transformation = Mat4.identity;
        Camera stationaryCamera = new StationaryCamera(PI/2, width, height, transformation);
        Background bg = null;
        try {
            bg = new Background(new BackgroundMaterial(new TransformedPicTexture("texture/skyPano.jpg", Mat4.scale(1,2,1))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new PicTexture("texture/gravel.jpg", black)));

        Group scene = null;
        scene = new Group(
            ground,
            bg
        );

        Group testSpheres = new Group(
            new Sphere(vec3(2,0,-.5), 1,new ReflectionMaterial(new PicTexture("texture/world.jpg", black),0)),
            new Sphere(vec3(-.5,0,-.8), 1, new DiffuseMaterial(new PicTexture("texture/world.jpg", black))),

            new Group(new Cone(vec3(-2,-1,-.4),.8,20, new DiffuseMaterial(new PicTexture("texture/wood.jpg", black)))),
            new Group(new Cylinder(vec3(1.5,-1,.6),.2,.8, new DiffuseMaterial(new PicTexture("texture/wood.jpg", black))))
        );
        Material m = new DiffuseMaterial(new TransformedPicTexture("texture/stone.jpg", Mat4.scale(vec3(.4))));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                testSpheres.addShape(
                    new Group(new Cube(vec3(-2.5,-.9,0), vec3(-2,-.75,.25), m), Mat4.translate(-.5*i,.15*i,.25*j))
                );
            }
        }
        scene.addShape(buildBVH(testSpheres.flattern(),1));

        ArrayList<Light> lights = new ArrayList<>();
        lights.add(new PointLight(vec3(0,8,0), vec3(50)));

        Sampler tracer = raytrace(
                new World(scene, lights)
                , stationaryCamera);

        long start_time = System.currentTimeMillis();
        renderImage(image, tracer);
        System.out.println(BoundingBox.misses);
        System.out.println((start_time-System.currentTimeMillis())/1000.0 + "s");
    }

    public static void renderImage(Image image, Sampler tracer){
       // SampleMultithread sampleMultithread = new SampleMultithread(image, tracer, (SAMPLING_RATE*8)/8,4);
        RayTraceFragmentExcecutor rayTraceFragmentExcecutor = new RayTraceFragmentExcecutor(threads);
        image = rayTraceFragmentExcecutor.executeTracer(image, (RayTracer) tracer, SAMPLING_RATE, xDim, yDim);
        //RayTraceExcecutor rayTraceExcecutor = new RayTraceExcecutor(4);
        //image = rayTraceExcecutor.executeTracer(image, (RayTracer) tracer, SAMPLING_RATE, 4,4);
        try {
            /*ImageMultithreadSocketWriter writer = null;
            Thread socketThread = null;

            if (WITH_SOCKET){
                writer   = new ImageMultithreadSocketWriter("saeftaher.de", 8770, image, tracer, (SAMPLING_RATE*4)/8);
                socketThread = new Thread(writer);
            }


            sampleMultithread.startMultiThreading();
            if (WITH_SOCKET) socketThread.start();

            if (WITH_SOCKET) socketThread.join();
            sampleMultithread.join();
            image = sampleMultithread.getImages();
            if (WITH_SOCKET) image = Image.mergeAVG(writer.RESULT_IMG, image);*/
        } catch ( Exception e) {
            e.printStackTrace();
        }
        
        image.sample(
            new GammaSampler(image, GAMMA)
        );

        try {
            System.out.println("Start writing image: " + filename);

            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }
    public static Group getImprovedSphereFlakeScene(Vec3 pos, double size, double scale, double depth){
        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new Vec3(0.7)));
        Vec3 p0 = new Vec3((size+size*scale), 0, 0),
                p1 = new Vec3(-(size+size*scale), 0, 0),
                p2 = new Vec3(0,  (size+size*scale), 0),
                p3 = new Vec3(0,  -(size+size*scale), 0),
                p4 = new Vec3(0, 0, (size+size*scale)),
                p5 = new Vec3(0, 0,  -(size+size*scale));
        Material m = new GlassMaterial(vec3(.8),1.2,0);
        Group initialGroup = new Group(
            new Sphere(vec3(0), size, m),
            new Sphere(p0, size*scale, new GlassMaterial(rndColor(),1.2,0)),
            new Sphere(p1, size*scale, new GlassMaterial(rndColor(),1.2,0)),
            new Sphere(p2, size*scale, new GlassMaterial(rndColor(),1.2,0)),
            new Sphere(p3, size*scale, new GlassMaterial(rndColor(),1.2,0)),
            new Sphere(p4, size*scale, new GlassMaterial(rndColor(),1.2,0)),
            new Sphere(p5, size*scale, new GlassMaterial(rndColor(),1.2,0))
        );

        Group g = new Group(
            new Sphere(vec3(0), size, m),
            sphereFlakeImproved(initialGroup, size, scale,depth)
        );
        g.setTransformation(Mat4.translate(pos));
        return g;
    }

    public static Group sphereFlakeImproved(Group spheres, double oldSize, double scale, double depth){
        if (depth == 0) return new Group();
        Vec3 p0 = new Vec3((oldSize+oldSize*scale), 0, 0),
             p1 = new Vec3(-(oldSize+oldSize*scale), 0, 0),
             p2 = new Vec3(0,  (oldSize+oldSize*scale), 0),
             p3 = new Vec3(0,  -(oldSize+oldSize*scale), 0),
             p4 = new Vec3(0, 0, (oldSize+oldSize*scale)),
             p5 = new Vec3(0, 0,  -(oldSize+oldSize*scale));
        Mat4 s = scale(vec3(scale));
        Group g0 = new Group(
                new Group(spheres),
                new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );
        Group g1 = new Group(
            new Group(spheres),
            new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );
        Group g2 = new Group(
                new Group(spheres),
                new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );
        Group g3 = new Group(
                new Group(spheres),
                new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );
        Group g4 = new Group(
                new Group(spheres),
                new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );
        Group g5 = new Group(
                new Group(spheres),
                new Group(sphereFlakeImproved(spheres, oldSize, scale, depth -1))
        );

        g0.setTransformation(Mat4.translate(p0).multiply(s));
        g1.setTransformation(Mat4.translate(p1).multiply(s));
        g2.setTransformation(Mat4.translate(p2).multiply(s));
        g3.setTransformation(Mat4.translate(p3).multiply(s));
        g4.setTransformation(Mat4.translate(p4).multiply(s));
        g5.setTransformation(Mat4.translate(p5).multiply(s));
        Group res = new Group(g1, g2,g3,g4,g5,g0);
        return res;
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
        double size = .016;
        return new Group(
                new Sphere(le_center, size, new GlassMaterial(rndColor(),1.5,.0)),
                new Sphere(newX, size, new GlassMaterial(rndColor(),1.5,.0)),
                new Sphere(newY, size, new GlassMaterial(rndColor(),1.5,.0)),
                new Sphere(newZ ,size, new GlassMaterial(rndColor(),1.5,.0))
        );
    }

    public static Group genSirpinskiScene(){

        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new Vec3(0.7)));

        Group scene = new Group(
                ground,
                getTriangleShape(
                    new Vec3(-.5,-.25,-1+.1), new Vec3(.5,-.25,-1+.1), new Vec3(0,-.25,-.5+.1), new Vec3(0, .25, -.75+.25+.1),
                    5
                )
        );
        scene.addShape(sirpinski(
            new Group(),
            new Vec3(-.5,-.25,-1+.1), new Vec3(.5,-.25,-1+.1), new Vec3(0,-.25,-.5+.1), new Vec3(0, .25, -.75+.25+.1),
            4
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

    public static Group genRubics(Vec3 v){
        Group cubeLayer1 = new Group(
                new Cube(.2, vec3(-1 +v.x, -.5 + v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.5+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-.45+v.x, -.5+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-1+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.45+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-1+1+v.x, -.5+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+1+v.x, -.5+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+1+v.x, -.5+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-1+1+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.75+1+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.45+1+v.x, -.5+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),

                new Cube(.2, vec3(-1+v.x, -.5+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.5+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+v.x, -.5+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-1+v.x, -.5+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.5+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+v.x, -.5+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .21))
        );

        Group cubeLayer2 = new Group(
                new Cube(.2, vec3(-1+v.x, -.25+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.75+v.x, -.25+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.45+v.x, -.25+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-1+v.x, -.25+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .0)),
                new Cube(.2, vec3(-.75+v.x, -.25+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+v.x, -.25+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1))

        );

        Group cubeLayer3 = new Group(
                new Cube(.2, vec3(-1+v.x, -.75+ v.y, -1.5-.5+ v.z),new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.75+ v.y, -1.5-.5+ v.z),new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+v.x, -.75+ v.y, -1.5-.5+ v.z),new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-1+v.x, -.75+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.75+v.x, -.75+ v.y, -1.75-.5+ v.z),new ReflectionMaterial(rndColor(), .1)),
                new Cube(.2, vec3(-.45+v.x, -.75+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(), .1)),

                new Cube(.2, vec3(-1+v.x, -.75+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-.75+v.x, -.75+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-.45+v.x, -.75+ v.y, -1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-1+v.x, -.75+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-.75+v.x, -.75+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),
                new Cube(.2, vec3(-.45+v.x, -.75+ v.y, -1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(), .2)),

                new Cube(.2, vec3(-1+1+v.x, -.75+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.75+1+v.x, -.75+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.45+1+v.x, -.75+ v.y, -1.5-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-1+1+v.x, -.75+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.75+1+v.x, -.75+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.45+1+v.x, -.75+ v.y, -1.75-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),

                new Cube(.2, vec3(-1+1+v.x, -.75+ v.y, 1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.0)),
                new Cube(.2, vec3(-.75+1+v.x, -.75+ v.y, 1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.0)),
                new Cube(.2, vec3(-.45+1+v.x, -.75+ v.y, 1.5+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.0)),
                new Cube(.2, vec3(-1+1+v.x, -.75+ v.y, 1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.0)),
                new Cube(.2, vec3(-.75+1+v.x, -.75+ v.y, 1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.1)),
                new Cube(.2, vec3(-.45+1+v.x, -.75+ v.y, 1.75+.7-.5+ v.z), new ReflectionMaterial(rndColor(),.0))
        );

        return new Group(cubeLayer1, cubeLayer2, cubeLayer3);

    }


    public static Group genSphereFractalScene(Vec3 pos, double size){
        Shape ground = new Plane(vec3(0.0, -1, 0.0), vec3(0, 1, 0), new DiffuseMaterial(new Vec3(0.7)));

        Group g = new Group(
            ground,
            /*new Cone(vec3(-3,-1,-3.5),2.5,10, new ReflectionMaterial(vec3(.5),0)),
            new Cone(vec3(2,-1,-3.5),2.5,10, new ReflectionMaterial(vec3(.8,.8,.1),0)),
            new Sphere(pos, size, new GlassMaterial(vec3(.8),1.1,0)),
            genCylinderCircle(vec3(pos.x,-1, pos.z),2.5,.12,.06, .8, 15),
            genCylinderCircle2(vec3(pos.x,-.8, pos.z),2.8,.2, .06, 20),
            genConeCircle(vec3(pos.x,-1, pos.z),2,.5,20, 40, 15)*/
            genSphereFractal(pos, size, 3)
        );
        return g;
    }

    public static Group genCylinderCircle(Vec3 pos, double radius, double radiusCylinder, double startHeight, double endHeight, double count){
        Group res = new Group();
        for(int i = 0; i < count; i++){
            Vec3 cur_pos = new Vec3(radius*sin(2*PI*i/count)+ pos.x, pos.y, radius*cos(2*PI*i/count) + pos.z);
            res.addShape(
                new Cylinder(cur_pos,radiusCylinder, startHeight*i/count+endHeight*(1-i/count), new ReflectionMaterial(rndColor(),0))
            );
        }
        return res;
    }

    public static Group genCylinderCircle2(Vec3 pos, double radius, double startHeight, double endHeight, double count) throws IOException {
        Group res = new Group();
        for(int i = 0; i < count; i++){
            Vec3 cur_pos = new Vec3(radius*sin(2*PI*i/count)+ pos.x, pos.y, radius*cos(2*PI*i/count) + pos.z);
            res.addShape(
                new Sphere(cur_pos, startHeight*i/count+endHeight*(1-i/count), new DiffuseMaterial(new PicTexture("texture/world.jpg", black)))
            );
        }
        return res;
    }

    public static Group genConeCircle(Vec3 pos, double radius, double height, double startDeg, double endDeg, double count){
        Group res = new Group();
        for(int i = 0; i < count; i++){
            Vec3 cur_pos = new Vec3(radius*sin(2*PI*i/count)+ pos.x, pos.y, radius*cos(2*PI*i/count) + pos.z);
            res.addShape(
                    new Cone(cur_pos, height, startDeg*i/count+endDeg*(1-i/count), new DiffuseMaterial(rndColor()))
            );
        }
        return res;
    }

    public static Group genSphereFractal(Vec3 oldPos, double oldSize, double depth){
        if (depth == 0) return new Group();
        double size = oldSize/2.2;
        Vec3 p0 = new Vec3(- oldSize - size, 0, 0),
             p1 = new Vec3( oldSize + size, 0, 0),
             p2 = new Vec3(0,  - oldSize - size, 0),
             p3 = new Vec3(0,  oldSize + size, 0),
             p4 = new Vec3(0, 0, - oldSize - size),
             p5 = new Vec3(0, 0, oldSize + size);

        Material m = new GlassMaterial(rndColor(),1.2,0);
        Group g = new Group(
            new Sphere(p0, size, m),
            new Sphere(p1, size, m),
            new Sphere(p2, size, m),
            new Sphere(p3, size, m),
            new Sphere(p4, size, m),
            new Sphere(p5, size, m),
            genSphereFractal(p0, size, depth - 1),
            genSphereFractal(p1, size, depth - 1),
            genSphereFractal(p2, size, depth - 1),
            genSphereFractal(p3, size, depth - 1),
            genSphereFractal(p4, size, depth - 1),
            genSphereFractal(p5, size, depth - 1)
        );
        g.setTransformation(Mat4.translate(oldPos));
        return g;
    }

    public static Vec3 rndColor(){
        return new Vec3(Math.random(), Math.random(), Math.random());
    }


}

