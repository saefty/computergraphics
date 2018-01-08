package taher858897.a05.Material;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.io.IOException;
import java.io.Serializable;

public interface Material extends Serializable{
    Vec3 emittedRadiance(Ray r, Hit h);
    Ray scatteredRay(Ray r, Hit h);
    Vec3 albedo(Ray r, Hit h);

    void loadTexture() throws IOException;

    boolean affectedByDirectLight();
}
