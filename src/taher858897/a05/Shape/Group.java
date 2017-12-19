package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static cgtools.Vec3.vec3;
import static java.lang.Math.min;

public class Group implements Shape {
    final ArrayList<Shape> shapes;
    private Cube hitBox;
    private Transformation transformation;

    public Group(ArrayList<Shape> shapes) {
        this.shapes = shapes;
        this.transformation = new Transformation(Mat4.identity);
        refreshHitBox();
    }

    public Group(ArrayList<Shape> shapes, Mat4 transformation) {
        this.shapes = shapes;
        this.transformation = new Transformation(transformation);
        refreshHitBox();
    }

    public Group() {
        this.shapes = new ArrayList<>();
        this.transformation = new Transformation(Mat4.identity);
        refreshHitBox();
    }

    public Group(Shape s1, Shape... sN){
        this.shapes = new ArrayList<>();
        this.shapes.add(s1);
        this.shapes.addAll(Arrays.asList(sN));
        this.transformation = new Transformation(Mat4.identity);
        refreshHitBox();
    }
    public Group(Shape s1, Mat4 transformation){
        this.shapes = new ArrayList<>();
        this.shapes.add(s1);
        this.transformation = new Transformation(transformation);
        refreshHitBox();
    }

    public void setTransformation(Mat4 transformation) {
        this.transformation = new Transformation(transformation);
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape e){
        if (e == null) return;
        shapes.add(e);
        refreshHitBox();
    }

    public void addShapes(Shape ... sN){
        shapes.addAll(Arrays.asList(sN));
        refreshHitBox();
    }

    public void addShapes(ArrayList sN){
        shapes.addAll(sN);
        refreshHitBox();
    }

    private void refreshHitBox(){
        double xMin = Double.POSITIVE_INFINITY,
               yMin = Double.POSITIVE_INFINITY,
               zMin = Double.POSITIVE_INFINITY;

        double xMax = Double.NEGATIVE_INFINITY,
               yMax = Double.NEGATIVE_INFINITY,
               zMax = Double.NEGATIVE_INFINITY;
        for (Shape s: shapes){
            Vec3 min = s.getMinPos();
            Vec3 max = s.getMaxPos();
            xMin = min(min.x, xMin);
            yMin = min(min.y, yMin);
            zMin = min(min.z, zMin);

            xMax = min(max.x, xMax);
            yMax = min(max.y, yMax);
            zMax = min(max.z, zMax);
        }
        Vec3 min = new Vec3(xMin -EPSILON,yMin-EPSILON,zMax+EPSILON);
        Vec3 max = new Vec3(xMax +EPSILON,yMax+EPSILON,zMin-EPSILON);
        if (min.length() < Double.POSITIVE_INFINITY && min.length() > Double.NEGATIVE_INFINITY &&
            max.length() < Double.POSITIVE_INFINITY && max.length() > Double.NEGATIVE_INFINITY){
            hitBox = new Cube(min, max, null);
        }
    }


    @Override
    public Hit intersect(Ray r) {
        r = transformation.transformRayToObject(r);
        if (hitBox != null){
            Hit hit_hitbox = hitBox.intersect(r);
            if (hit_hitbox == null) return null;
        }
        Hit nearest = null;
        for (Shape s: shapes) {
            Hit h = s.intersect(r);
            if (nearest == null) {
                nearest = h;
                continue;
            }
            if (h != null && h.t <= nearest.t)
                nearest = h;
        }
        if (nearest != null){
            nearest = transformation.transformHitToWorld(nearest);
        }
        return nearest;
    }

    @Override
    public Vec3 getMinPos() {
        return this.hitBox != null ? hitBox.getMinPos() : vec3(Double.NEGATIVE_INFINITY);
    }

    @Override
    public Vec3 getMaxPos() {
        return this.hitBox != null ? hitBox.getMaxPos() : vec3(Double.POSITIVE_INFINITY);
    }
}
