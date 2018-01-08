package taher858897.a05.Textures;

import cgtools.Vec3;
import taher858897.a05.Main;

public class ConstantTexture implements Texture {
    public final Vec3 color;

    public ConstantTexture(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 getConstant() {
        return color;
    }

    @Override
    public Vec3 getPicture(Vec3 uv) {
        return color;
    }

    @Override
    public void loadTexture() {
    }
}
