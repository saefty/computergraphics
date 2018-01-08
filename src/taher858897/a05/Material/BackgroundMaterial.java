package taher858897.a05.Material;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;
import taher858897.a05.Textures.ConstantTexture;
import taher858897.a05.Textures.Texture;

import java.io.IOException;

public class BackgroundMaterial implements Material{
    final Texture texture;

    public BackgroundMaterial(Vec3 color) {
        this.texture = new ConstantTexture(color);
    }

    public BackgroundMaterial(Texture texture) {
        this.texture = texture;
    }


    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        if (h.textureCords == null) {
            return texture.getConstant();
        } else {
            return texture.getPicture(h.textureCords);
        }
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        return null;
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return Vec3.black;
    }

    @Override
    public void loadTexture() throws IOException {
        texture.loadTexture();
    }

    @Override
    public boolean affectedByDirectLight() {
        return true;
    }
}
