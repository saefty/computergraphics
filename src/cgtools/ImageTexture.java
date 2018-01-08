package cgtools;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import static cgtools.Vec3.*;
import static taher858897.a05.Main.threads;

public class ImageTexture implements Serializable{
    private transient BufferedImage image;
    public final String imgName;
    public transient int width;
    public transient int height;
    private transient double[] pixelBuffer;
    private transient double componentScale;

    public static ArrayList<ImageTexture> imageTextures = new ArrayList<>();

    public ImageTexture(String filename) {
        imageTextures.add(this);
        imgName = filename;
    }

    public void load() throws IOException {
        if (image != null) return;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        image = ImageIO.read(classLoader.getResource(imgName).openStream());
        width = image.getWidth();
        height = image.getHeight();
        pixelBuffer = new double[image.getRaster().getNumBands()];

        switch (image.getSampleModel().getDataType()) {
            case DataBuffer.TYPE_BYTE:
                componentScale = 255;
                break;
            case DataBuffer.TYPE_USHORT:
                componentScale = 65535;
                break;
            default:
                componentScale = 1;
                break;
        }
    }


    public Vec3 samplePoint(double u, double v) {
        int x = (int) ((u - Math.floor(u)) * width);
        int y = (int) ((v - Math.floor(v)) * height);
        image.getRaster().getPixel(x, y, pixelBuffer);
        Vec3 color = vec3(pixelBuffer[0], pixelBuffer[1], pixelBuffer[2]);
        return divide(color, componentScale);
    }
}
