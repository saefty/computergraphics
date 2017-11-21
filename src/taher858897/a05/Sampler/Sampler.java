package taher858897.a05.Sampler;

import cgtools.Vec3;

import java.io.Serializable;

public interface Sampler extends Serializable {
    public Vec3 color(double x, double y);
}
