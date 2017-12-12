package taher858897.a05.Camera;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Ray;

import static java.lang.Math.*;

public class PanoramaCamera implements Camera {
    public final double phi;
    public final double pic_width;
    public final double pic_height;
    public final Mat4 tranformation;

    public PanoramaCamera(double phi, double pic_width, double pic_height, Mat4 tranformation) {
        this.phi = phi;
        this.pic_width = pic_width;
        this.pic_height = pic_height;
        this.tranformation = tranformation;
    }

    public PanoramaCamera(double phi, double pic_width, double pic_height) {
        this.phi = phi;
        this.pic_width = pic_width;
        this.pic_height = pic_height;
        this.tranformation = null;
    }

    @Override
    public Ray generateRay(double x, double y) {

        Vec3 o = new Vec3(0,0,0);
        Vec3 d = Vec3.normalize(new Vec3(
                - cos(2*PI*(x/pic_width)),
                Math.cos(PI*(y/(pic_height))),
                - sin(2*PI*(x/pic_width))
        ));
        if (tranformation != null) {
            o = tranformation.transformPoint(o);
            d = tranformation.transformDirection(d);
        }
        return new Ray(o, d,0, Double.POSITIVE_INFINITY);
    }

}
