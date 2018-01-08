package taher858897.a05.RayTracer;

import cgtools.Vec3;
import taher858897.a05.RayTracer.World;

import java.io.Serializable;

public interface Light extends Serializable{
    public class Sample {
        // Direction to light source
        public Vec3 direction;
        public Vec3 emission;
        public Vec3 color;

        public Sample(Vec3 d, Vec3 e, Vec3 color){
            this.direction = d;
            this.emission = e;
            this.color = color;
        }


        public Sample(Vec3 d, Vec3 e){
            this.direction = d;
            this.emission = e;
            this.color = Vec3.white;
        }
    }

    public Sample sample(World world, Hit hitPoint, boolean intersect);
}