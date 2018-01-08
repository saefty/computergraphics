package taher858897.a05.Textures;

import cgtools.Vec3;
import taher858897.a05.Sampler.Sampler;

import java.io.IOException;
import java.io.Serializable;

public interface Texture extends Serializable {
    Vec3 getConstant();
    Vec3 getPicture(Vec3 uv);

    void loadTexture() throws IOException;
}
