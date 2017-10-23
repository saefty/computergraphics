package taher858897;

import cgtools.ImageWriter;
import cgtools.Vec3;

import java.io.IOException;
import java.util.HashMap;

public class Image {
    private double[] image;
    private int width;
    private int height;

    public Image(int width, int height) {
        image = new double[width*height*3];
        this.width = width;
        this.height = height;
    }

    public void setPixel(int x, int y, Vec3 color) {
        int index = (y * this.width + x) * 3;
        image[index] = color.x;
        image[index + 1] = color.y;
        image[index + 2] = color.z;
    }

    public void write(String filename) throws IOException {
        ImageWriter w = new ImageWriter(image, width, height);
        w.write(filename);
    }

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

}
