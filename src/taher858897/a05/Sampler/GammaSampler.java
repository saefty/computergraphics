package taher858897.a05.Sampler;

import cgtools.Vec3;

public class GammaSampler implements Sampler {
    final Sampler s;
    final double GAMMA;

    public GammaSampler(Sampler s, double gamma) {
        this.s = s;
        GAMMA = gamma;
    }
    public Vec3 gamma(Vec3 vec3) {
        return new Vec3(
                Math.pow(vec3.x, 1.0 / GAMMA),
                Math.pow(vec3.y, 1.0 / GAMMA),
                Math.pow(vec3.z, 1.0 / GAMMA));
    }

    @Override
    public Vec3 color(double x, double y) {
        return gamma(s.color(x, y));
    }
}
