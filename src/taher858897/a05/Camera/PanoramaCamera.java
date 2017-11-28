package taher858897.a05.Camera;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Ray;

import static java.lang.Math.cos;

public class PanoramaCamera implements Camera {
    public final double phi;
    public final double pic_width;
    public final double pic_height;
    public final Mat4 tranformation;

    private final double pre_x;
    private final double pre_y;
    private final double pre_z;

    public PanoramaCamera(double phi, double pic_width, double pic_height, Mat4 tranformation) {
        this.phi = phi;
        this.pic_width = pic_width;
        this.pic_height = pic_height;
        this.tranformation = tranformation;
        this.pre_x = pic_width / 2.0;
        this.pre_y = pic_height / 2.0;
        this.pre_z =  - (pic_height / 2.0) / (Math.tan(phi / 2.0));
    }

    public PanoramaCamera(double phi, double pic_width, double pic_height) {
        this.phi = phi;
        this.pic_width = pic_width;
        this.pic_height = pic_height;
        this.tranformation = null;
        this.pre_x = pic_width / 2.0;
        this.pre_y = pic_height / 2.0;
        this.pre_z =  - (pic_height / 2.0) / (Math.tan(phi / 2.0));
    }

    @Override
    public Ray generateRay(double x, double y) {
        x = x/cos(20) + 1;
        y = (y + 20);
        Vec3 o = new Vec3(0);
        Vec3 d = Vec3.normalize(new Vec3(x-this.pre_x, this.pre_y-y, this.pre_z));
        if (tranformation != null){
            o = tranformation.transformPoint(o);
            d = tranformation.transformDirection(d);
        }
        return new Ray(o, d,0, Double.POSITIVE_INFINITY);
    }

}
