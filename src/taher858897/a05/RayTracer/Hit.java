package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.Material.Material;

import java.io.Serializable;

public class Hit implements Serializable{
    public double t;
    public Vec3 normVec;
    public final Vec3 position;
    public final Vec3 textureCords;
    public Material material;

    public Hit(double t, Vec3 normVec, Vec3 position, Vec3 textureCords, Material material) {
        this.t = t;
        this.normVec = normVec;
        this.position = position;
        this.material = material;
        this.textureCords = textureCords;
    }

}
