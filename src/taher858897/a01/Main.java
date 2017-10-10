package taher858897.a01;

import cgtools.Vec3;
import static cgtools.Vec3.*;
import java.io.IOException;
import taher858897.Image;

public class Main {
    static int width = 720;
    static int height = 480;

    public static void main(String[] args) {
        Image image = new Image(width, height);

        for (int x = 0; x != width; x++) {
            for (int y = 0; y != height; y++) {
                image.setPixel(x, y, pixelColor(x, y));
            }
        }

        String filename = "doc/a01.png";
        try {
            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

    static Vec3 pixelColor(int x, int y) {
        double transition = ((double) x)/width;
        return vec3(0+0.3*transition, 0.4+0.1*transition, 0.1+0.8*transition);

    }
}
