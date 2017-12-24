package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

public class Transformation {
    private final Mat4 M;
    private final Mat4 MInvertedTransposed;
    private final Mat4 MInverted;

    public Transformation(Mat4 transformation){
        M = transformation;
        MInverted = transformation.invertFull();
        MInvertedTransposed = MInverted.transpose();
    }

    public Ray transformRayToObject(Ray r){
        return new Ray(
            transfromPointToObject(r.o),
            transfromDirectionToObject(r.d),
            r.t0,
            r.t1
        );
    }

    public Hit transformHitToWorld(Hit h){
        return new Hit(
            h.t,
            transformNormVecToWorld(h.normVec),
            transformPointToWorld(h.position),
            h.textureCords,
            h.material
        );
    }

    public Vec3 transformPointToWorld(Vec3 in) {
        return  M.transformPoint(in);
    }

    public Vec3 transformDirectionToWorld(Vec3 in) {
        return  M.transformDirection(in);
    }

    public Vec3 transformNormVecToWorld(Vec3 in) {
        return  MInvertedTransposed.transformDirection(in);
    }

    public Vec3 transfromDirectionToObject(Vec3 in){
        return MInverted.transformDirection(in);
    }

    public Vec3 transfromPointToObject(Vec3 in){
        return MInverted.transformPoint(in);
    }

    public Mat4 getM() {
        return M;
    }
}
