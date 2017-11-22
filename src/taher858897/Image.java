package taher858897;

import cgtools.ImageWriter;
import cgtools.Vec3;
import taher858897.a05.Sampler.Sampler;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Image implements Serializable, Sampler{
    private double[] image;
    private int width;
    private int height;
    public transient Object[] index_locks;

    public Image(int width, int height) {
        image = new double[width*height*3];
        /*index_locks = new Object[width*height*3];
        for (int i = 0; i < index_locks.length; i++) {
            index_locks[i] = new Object();
        }*/
        this.width = width;
        this.height = height;
    }

    /**
     * Copy ctor
     * @param i
     */
    public Image(Image i){
        width = i.width;
        height = i.height;
        image = new double[width*height*3];
    }

    public void merge(Image img, int x_start, int x_end, int y_start, int y_end){
        for (int x = x_start; x < x_end; x++) {
            for (int y = y_start; y < y_end; y++) {
                this.setPixel(x, y, img.getPixel(x, y));
            }
        }
    }

    public void setPixel(int x, int y, Vec3 color) {
        int index = (y * this.width + x) * 3;
        //synchronized (index_locks[index]){
            image[index] = color.x;
            image[index + 1] = color.y;
            image[index + 2] = color.z;
        //}
    }

    public Vec3 getPixel(int x, int y){
        int index = (y * this.width + x) * 3;
        return new Vec3(
            image[index],
            image[index + 1],
            image[index + 2]
        );
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

    public static Image mergeAVG(Image ... images) {
        double size = images.length;
        Image result = new Image(images[0]);
        for (int x = 0; x < result.width; x++) {
            for (int y = 0; y < result.height; y++) {
                double sumR = 0;
                double sumG = 0;
                double sumB = 0;
                for (Image i : images){
                    if (i == null)
                        return null;
                    Vec3 color = i.getPixel(x,y);
                    sumR += color.x;
                    sumG += color.y;
                    sumB += color.z;
                }
                result.setPixel(x, y, new Vec3(sumR/size, sumG/size, sumB/size));
            }
        }
        return result;
    }

    @Override
    public Vec3 color(double x, double y) {
        return getPixel((int)x, (int)y);
    }
}
