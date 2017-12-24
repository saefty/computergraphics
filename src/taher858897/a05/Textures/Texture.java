package taher858897.a05.Textures;

import cgtools.Vec3;
import taher858897.a05.Sampler.Sampler;

public interface Texture {
    Vec3 getConstant();
    Vec3 getPicture(Vec3 uv);
}
