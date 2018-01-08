package taher858897.a05.Textures;

import cgtools.ImageTexture;
import cgtools.Vec3;
import taher858897.a05.Sampler.Sampler;

import java.io.IOException;


public class PicTexture implements Texture {
    public final ImageTexture imageTexture;
    public final Vec3 constant;

    public PicTexture(ImageTexture imageTexture, Vec3 constant) {
        this.imageTexture = imageTexture;
        this.constant = constant;
    }
    public PicTexture(String file, Vec3 constant) throws IOException {
        this.imageTexture = new ImageTexture(file);
        this.constant = constant;
    }

    @Override
    public Vec3 getConstant() {
        return constant;
    }

    @Override
    public Vec3 getPicture(Vec3 uv) {
        return imageTexture.samplePoint(uv.x, uv.y);
    }

    @Override
    public void loadTexture() throws IOException {
        imageTexture.load();
    }
}
