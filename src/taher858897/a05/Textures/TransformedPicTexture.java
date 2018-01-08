package taher858897.a05.Textures;

import cgtools.ImageTexture;
import cgtools.Mat4;
import cgtools.Vec3;

import java.io.IOException;


public class TransformedPicTexture implements Texture {
    public final ImageTexture imageTexture;
    public final Mat4 transformation;

    public TransformedPicTexture(ImageTexture imageTexture, Mat4 transformation) {
        this.imageTexture = imageTexture;
        this.transformation = transformation;
    }
    public TransformedPicTexture(String file, Mat4 transformation) throws IOException {
        this.imageTexture = new ImageTexture(file);
        this.transformation = transformation;
    }

    @Override
    public Vec3 getConstant() {
        return null;
    }

    @Override
    public Vec3 getPicture(Vec3 uv) {
        Vec3 transformed = transformation.transformPoint(uv);
        return imageTexture.samplePoint(transformed.x, transformed.y);
    }

    @Override
    public void loadTexture() throws IOException {
        imageTexture.load();
    }
}
