package taher858897.a05.Tests;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;
import taher858897.a05.Shape.Sphere;

import static cgtools.Vec3.black;

public class RayTestMain {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        //test4();
    }

    public static void test1(){
        Sphere s;
        Ray r;
        Hit h;
        s = new Sphere(
                new Vec3(0,0,-2),
                1,
                black
        );
        r = new Ray(new Vec3(0,0,0), new Vec3(0,0,-1),0, Double.MAX_VALUE);
        h = s.intersect(r);
        assert(h != null);
        assert(new Vec3(0,0,-1).equals(h.position));
        assert(new Vec3(0,0,1).equals(h.normVec));
        System.out.println("Test 1 successfull");
    }

    public static void test2(){
        Sphere s;
        Ray r;
        Hit h;
        s = new Sphere(
                new Vec3(0,0,-2),
                1,
                black
        );
        r = new Ray(new Vec3(0,0,0), new Vec3(0,1,-1),0, Double.MAX_VALUE);
        h = s.intersect(r);
        assert(h == null);
        System.out.println("Test 2 successfull");

    }

    public static void test3(){
        Sphere s;
        Ray r;
        Hit h;
        s = new Sphere(
                new Vec3(0,-1,-2),
                1,
                black
        );
        r = new Ray(new Vec3(0,0,0), new Vec3(0,0,-1),0, Double.MAX_VALUE);
        h = s.intersect(r);
        System.out.println(h.position);
        assert(h != null);
        assert(new Vec3(0,0,-2).equals(h.position));
        assert(new Vec3(0,1,0).equals(h.normVec));
        System.out.println("Test 3 successfull");

    }

    public static void test4(){
        Sphere s;
        Ray r;
        Hit h;
        s = new Sphere(
                new Vec3(0,0,-2),
                1,
                black
        );
        r = new Ray(new Vec3(0,0,-2), new Vec3(0,0,-1),0, Double.MAX_VALUE);
        h = s.intersect(r);
        assert(h == null);
        System.out.println("Test 4 successfull");

    }
}
