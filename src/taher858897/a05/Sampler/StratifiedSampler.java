package taher858897.a05.Sampler;

import cgtools.Vec3;

import static cgtools.Vec3.*;

public class StratifiedSampler implements Sampler {
    Sampler sampler;
    int n;

    public StratifiedSampler(Sampler sampler, int n) {
        this.sampler = sampler;
        this.n = (int) Math.ceil(Math.sqrt(n));
    }

    public Vec3 color(double x, double y) {
        Vec3 color = vec3(0, 0, 0);
        for (int xi = 0; xi < n; xi++) {
            for (int yi = 0; yi < n; yi++) {
                double rx = Math.random();
                double ry = Math.random();
                double xs = x + (xi + rx) / n;
                double ys = y + (yi + ry) / n;
                color = add(color, sampler.color(xs, ys));
            }
        }
        return divide(color, n * n);
    }
}
