package taher858897;

import cgtools.ImageWriter;
import cgtools.Vec3;
import taher858897.a05.Sampler.Sampler;

import java.io.IOException;
import java.util.HashMap;

public class Image {
    private double[] image;
    private int width;
    private int height;
    private Object[] index_locks;

    public Image(int width, int height) {
        image = new double[width*height*3];
        index_locks = new Object[width*height*3];
        for (int i = 0; i < index_locks.length; i++) {
            index_locks[i] = new Object();
        }
        this.width = width;
        this.height = height;
    }

    public void setPixel(int x, int y, Vec3 color) {
        int index = (y * this.width + x) * 3;
        synchronized (index_locks[index]){
            image[index] = color.x;
            image[index + 1] = color.y;
            image[index + 2] = color.z;
        }
    }

    public void sample(Sampler sampler){
        sample(sampler, 0, width, 0, height);
    }

    public void sample(Sampler sampler, int x_start, int x_end, int y_start, int y_end){
        for (int x = x_start; x != x_end; x++) {
            for (int y = y_start; y != y_end; y++) {
                setPixel(x, y, sampler.color(x, y));
            }
        }
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
