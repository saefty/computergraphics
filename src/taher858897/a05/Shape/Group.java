package taher858897.a05.Shape;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static cgtools.Vec3.vec3;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Group implements Shape {
    final ArrayList<Shape> shapes;
    private Cube hitBox;
    private BoundingBox boundingBox;
    private Transformation transformation;

    public Group(ArrayList<Shape> shapes) {
        this.shapes = shapes;
        this.transformation = new Transformation(Mat4.identity);
    }

    public Group(ArrayList<Shape> shapes, Mat4 transformation) {
        this.shapes = shapes;
        this.transformation = new Transformation(transformation);
    }

    public Group() {
        this.shapes = new ArrayList<>();
        this.transformation = new Transformation(Mat4.identity);
    }

    public Group(Shape s1, Shape... sN){
        this.shapes = new ArrayList<>();
        this.shapes.add(s1);
        this.shapes.addAll(Arrays.asList(sN));
        this.transformation = new Transformation(Mat4.identity);
    }
    public Group(Shape s1, Mat4 transformation){
        this.shapes = new ArrayList<>();
        this.shapes.add(s1);
        this.transformation = new Transformation(transformation);
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
    }

    public void addShapes(Shape ... sN){
        shapes.addAll(Arrays.asList(sN));
    }

    public void addShapes(ArrayList sN){
        shapes.addAll(sN);
    }

    public ArrayList<Group> flattern(){
        return flattern(Mat4.identity);
    }

    private ArrayList<Group> flattern(Mat4 mat){
        ArrayList<Group> result = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape instanceof Group){
                result.addAll(((Group) shape).flattern(mat.multiply(transformation.getM())));
            }else {
                result.add(new Group(shape, mat.multiply(transformation.getM())));
            }
        }
        return result;
    }

    public int nodes(){
        int sum = 1;
        for (Shape shape : shapes) {
            if (shape instanceof Group){
                sum += ((Group) shape).nodes();
            }
        }
        return sum;
    }
    public int maxDepth(){
        int curMaxDepth = 0;
        ArrayList<Group> groups = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape instanceof Group){
                groups.add((Group) shape);
            }
        }
        if (groups.size() == 0){
            return 0;
        }
        for (Group g : groups) {
            curMaxDepth = max(g.maxDepth(), curMaxDepth);
        }
        return curMaxDepth + 1;
    }

    static public Group buildBVH(ArrayList<Group> groups, double overlap){
        if (groups.size() <= overlap || groups.size() <= 2){
            Group g = new Group();
            g.addShapes(groups);
            g.boundingBox = g.bounds();
            return g;
        }
        BoundingBox bvh = new BoundingBox(vec3(0), vec3(0));
        for (Group g: groups) {
            bvh = bvh.extend(g.bounds());
        }
        BoundingBox boundingBoxLeft = bvh.splitLeft();
        BoundingBox boundingBoxRight = bvh.splitRight();

        ArrayList<Group> left = new ArrayList<>();
        ArrayList<Group> right = new ArrayList<>();

        Group res = new Group();
        for (Group g: groups) {
            if (boundingBoxLeft.contains(g.bounds())){
                left.add(g);
            }
            else if (boundingBoxRight.contains(g.bounds())){
                right.add(g);
            }
            else {
                res.addShape(g);
            }
        }
        if (left.size() > 0){
            res.addShapes(buildBVH(left, overlap));
        }
        if (right.size() > 0){
            res.addShapes(buildBVH(right, overlap));
        }
        res.boundingBox = res.bounds();
        return res;
    }

    @Override
    public Hit intersect(Ray r) {
        if (boundingBox != null && !boundingBox.intersect(r)){
            return null;
        }
        r = transformation.transformRayToObject(r);
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
    public BoundingBox bounds() {
        BoundingBox bb = new BoundingBox(vec3(0), vec3(0));
        for (Shape s: shapes){
            bb = bb.extend(s.bounds().transform(transformation.getM()));
        }
        return bb;
    }

    @Override
    public boolean contains(Vec3 pos) {
        return false;
    }
    @Override
    public String toString() {
        return "Group:" + shapes.toString();
    }
}
