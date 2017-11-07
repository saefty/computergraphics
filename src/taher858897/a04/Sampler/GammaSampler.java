package taher858897.a04.Sampler;

import cgtools.Vec3;

public class GammaSampler implements Sampler {
    final Sampler s;
    final double GAMMA;

    public GammaSampler(Sampler s, double gamma) {
        this.s = s;
        GAMMA = gamma;
    }
    public Vec3 gamma(Vec3 vec3) {
        double x = Math.pow(vec3.x, 1.0 / GAMMA);
        double y = Math.pow(vec3.y, 1.0 / GAMMA);
        double z = Math.pow(vec3.z, 1.0 / GAMMA);
        return new Vec3(x, y, z);
    }

    @Override
    public Vec3 color(double x, double y) {
        return gamma(s.color(x, y));
    }
}
