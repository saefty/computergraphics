package taher858897.a05.Material;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;
import taher858897.a05.Textures.ConstantTexture;
import taher858897.a05.Textures.Texture;

import static cgtools.Vec3.dotProduct;
import static cgtools.Vec3.normalize;
import static taher858897.a05.Shape.Shape.EPSILON;

public class ReflectionMaterial implements Material{
    final Texture texture;
    final double rndFactor;

    public ReflectionMaterial(Vec3 color, double rnd_factor) {
        this.texture = new ConstantTexture(color);
        this.rndFactor = rnd_factor;
    }
    public ReflectionMaterial(Texture texture, double rndFactor) {
        this.texture = texture;
        this.rndFactor = rndFactor;
    }


    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return Vec3.black;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        Vec3 reflectedDirection = normalize(reflectedDirection(r, h));
        return new Ray(h.position, reflectedDirection, EPSILON, Double.POSITIVE_INFINITY);
    }

    public Vec3 reflectedDirection(Ray r, Hit h){
        Vec3 refl = Vec3.multiply(-2 * dotProduct(h.normVec, r.d), h.normVec);
        return Vec3.addFast(refl, r.d, Vec3.multiplyFast(rndFactor, Vec3.randomDirection()));
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        if (h.textureCords == null) {
            return texture.getConstant();
        } else {
            return texture.getPicture(h.textureCords);
        }
    }
}
