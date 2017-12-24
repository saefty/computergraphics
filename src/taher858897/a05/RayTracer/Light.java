package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.RayTracer.World;

public interface Light {
    public class Sample {
        // Direction to light source
        public Vec3 direction;
        public Vec3 emission;

        public Sample(Vec3 d, Vec3 e){
            this.direction = d;
            this.emission = e;
        }
    }

    public Sample sample(World world, Hit hitPoint, boolean intersect);
}