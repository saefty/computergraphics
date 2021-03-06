package taher858897.a01;

import cgtools.Vec3;
import static cgtools.Vec3.*;
import java.io.IOException;
import taher858897.Image;

public class Main {
    static int width = 160;
    static int height = 160;

    public static void main(String[] args) {
        Image image = new Image(width, height);

        for (int x = 0; x != width; x++) {
            for (int y = 0; y != height; y++) {
                image.setPixel(x, y, pixelColor(x, y));
            }
        }

        String filename = "doc/a01-checkerboard.png";
        try {
            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }
    }

    static Vec3 pixelColor(int x, int y) {
        double linear_transistion = ((double) x )/width;

        if (x % 16 < 8 && y % 16 < 8)
            return  vec3(0,0,0);

        if (x % 16 >= 8 && y % 16 >= 8)
            return  vec3(0,0,0);
        return vec3(0 + 0.3 * linear_transistion, 0.4 + 0.1 * linear_transistion, 0.1 + 0.8 * linear_transistion);
    }
}
