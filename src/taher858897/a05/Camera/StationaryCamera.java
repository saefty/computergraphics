package taher858897.a05.Camera;

import cgtools.Vec3;
import taher858897.a05.RayTracer.Ray;

public class StationaryCamera implements Camera {
    public final double phi;
    public final double pic_width;
    public final double pic_height;

    public StationaryCamera(double phi, double pic_width, double pic_height) {
        this.phi = phi;
        this.pic_width = pic_width;
        this.pic_height = pic_height;
    }

    @Override
    public Ray generateRay(double x, double y){
        Vec3 o = new Vec3(0);
        Vec3 d = genDirection(x,y);
        return new Ray(o,d,0, Double.MAX_VALUE);
    }

    private Vec3 genDirection(double x, double y){
        double x_d = x - pic_width / 2.0;
        double y_d = pic_height / 2.0 - y;
        double z_d = - (pic_height / 2.0) / (Math.tan(phi / 2.0));
        return Vec3.normalize(new Vec3(x_d, y_d, z_d));
    }
}
