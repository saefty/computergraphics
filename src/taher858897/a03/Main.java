package taher858897.a03;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.Image;

import java.io.IOException;
import java.util.ArrayList;

import static cgtools.Vec3.black;
import static cgtools.Vec3.vec3;

public class Main {
    private static int width  = 160 * 12;
    private static int height = 90 * 12;

    private static final double GAMMA = 2.2;
    private static final int SAMPLING_RATE = 10; // SAMPLING = RATE^2

    private static StationaryCamera stationaryCamera = new StationaryCamera(Math.PI/2, width, height);
    private static Sphere sphere = new Sphere(new Vec3(0,0,-200),100, black);

    public static void main(String[] args) {
        Image image = new Image(width, height);
        for (int x = 0; x != width; x++) {
            for (int y = 0; y != height; y++) {
                Vec3 vec3 = supersampleRayTrace(x, y);
                vec3 = gamma(vec3);
                image.setPixel(x, y, vec3);
            }
        }


        String filename = "doc/a03-one-sphere.png";
        try {
            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }


    public static Vec3 supersampleRayTrace(double x, double y){
        Ray r = stationaryCamera.generateRay(x, y);
        Hit h = sphere.intersect(r);
        Vec3 result = notNullColor(h);

        double rx, ry, xs, ys;
        for (int xi = 0; xi < SAMPLING_RATE; xi++) {
            for (int yi = 0; yi < SAMPLING_RATE; yi++) {
                rx = Random.random();
                ry = Random.random();
                xs = x + (xi + rx) / SAMPLING_RATE;
                ys = y + (yi + ry) / SAMPLING_RATE;
                r = stationaryCamera.generateRay(xs, ys);
                h = sphere.intersect(r);
                result = Vec3.add(result, notNullColor(h));
            }
        }
        Vec3 tmp = Vec3.divide(result, SAMPLING_RATE * SAMPLING_RATE);
        return tmp;
    }

    static Vec3 notNullColor(Hit hit){
        if (hit == null){
            return new Vec3(0);
        }
        return hit.normVec;
    }

    static Vec3 gamma(Vec3 vec3) {
        double x = Math.pow(vec3.x, 1.0 / GAMMA);
        double y = Math.pow(vec3.y, 1.0 / GAMMA);
        double z = Math.pow(vec3.z, 1.0 / GAMMA);
        return new Vec3(x, y, z);
    }


    static Vec3 supersample(double x, double y) {
        Vec3 result = pixelColor(x, y);
        double rx, ry, xs, ys;
        for (int xi = 0; xi < SAMPLING_RATE; xi++) {
            for (int yi = 0; yi < SAMPLING_RATE; yi++) {
                rx = Random.random();
                ry = Random.random();
                xs = x + (xi + rx) / SAMPLING_RATE;
                ys = y + (yi + ry) / SAMPLING_RATE;
                result = Vec3.add(result, pixelColor(xs, ys));
            }
        }
        Vec3 tmp = Vec3.divide(result, SAMPLING_RATE * SAMPLING_RATE);
        return tmp;
    }

    static Vec3 linear_horizontal_transistion(Vec3 start, Vec3 end, int x) {
        double linear_transistion = ((double) x) / width;
        Vec3 direction = Vec3.subtract(end, start);
        return Vec3.add(start, Vec3.multiply(linear_transistion, direction));
    }


    static Vec3 pixelColor(double x, double y) {
        Vec3 direction = stationaryCamera.generateRay(x, y).d;
        double sq_factor_x = 1 / 16.0;
        double sq_factor_y = 1 / 32.0;

        Vec3 color = null;
        if (Math.abs(direction.x) % sq_factor_x <= sq_factor_x/2 && Math.abs(direction.y) % sq_factor_y <= sq_factor_y/2)
            color = vec3(0,0,0);
        if (Math.abs(direction.x) % sq_factor_x > sq_factor_x/2 && Math.abs(direction.y) % sq_factor_y > sq_factor_y/2)
            color = vec3(0,0,0);

        double hue_val = (Math.abs(direction.y) + Math.abs(direction.x))/2.0;

        if (color == null){
            color = Vec3.hue(hue_val);
        }
        return color;
    }

}
