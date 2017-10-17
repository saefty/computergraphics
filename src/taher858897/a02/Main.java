package taher858897.a02;

import cgtools.Random;
import cgtools.Vec3;
import taher858897.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static cgtools.Vec3.red;
import static cgtools.Vec3.vec3;

public class Main {
    private static int width  = 160 * 3;
    private static int height = 90 * 3;

    private static final double GAMMA = 2.2;
    private static final int SAMPLING_RATE = 10; // SAMPLING = RATE^2

    private static ArrayList<Circle> circles = new ArrayList<>();
    private static final int CIRCLE_AMOUNT = 100;

    public static void main(String[] args) {
        Image image = new Image(width, height);

        for (int i = 0; i < CIRCLE_AMOUNT; i++) {
            Circle c = rndCircle();
            circles.add(c);
        }

        circles.sort(Comparator.comparingDouble(Circle::getRadius));

        for (int x = 0; x != width; x++) {
            for (int y = 0; y != height; y++) {
                image.setPixel(x, y, gamma(supersample(x, y)));
            }
        }

        String filename = "doc/a02-super-sampling.png";
        try {
            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

    static Vec3 gamma(Vec3 vec3) {
        double x = Math.pow(vec3.x, 1.0 / GAMMA);
        double y = Math.pow(vec3.y, 1.0 / GAMMA);
        double z = Math.pow(vec3.z, 1.0 / GAMMA);
        return new Vec3(x, y, z);
    }

    static Vec3 linear_horizontal_transistion(Vec3 start, Vec3 end, int x) {
        double linear_transistion = ((double) x) / width;
        Vec3 direction = Vec3.subtract(end, start);
        return Vec3.add(start, Vec3.multiply(linear_transistion, direction));
    }

    static Vec3 supersample(int x, int y) {
        System.out.println(x);
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

    static Circle rndCircle() {
        int x = (int) (Random.random() * width);
        int y = (int) (Random.random() * width);
        int radius = Math.max((int) (Random.random() * 0.1 * width) + 10, (int) (0.02 * width));

        double r = Random.random() * Random.random();
        double g = Random.random() * Random.random();
        double b = Random.random() * Random.random();

        return new Circle(x, y, radius, new Vec3(r, g, b));
    }

    static Vec3 pointSample(double x, double y) {
        return pixelColor(x + 0.5, y + 0.5);
    }

    static Vec3 pixelColor(double x, double y) {
        Vec3 color = null;
        for (Circle c : circles) {
            if (c.coordinateInCircle(x, y)) {
                color = c.getColor();
                break;
            }
        }

        if (color == null) {
            Vec3 start_color = new Vec3(0.0, 0.0, 1);
            Vec3 end_color = new Vec3(0.0, 1, 0.0);
            color = linear_horizontal_transistion(start_color, end_color, (int) x);
        }

        return color;
    }

}
