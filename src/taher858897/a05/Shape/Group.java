package taher858897.a05.Shape;

import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.util.ArrayList;
import java.util.Arrays;

public class Group implements Shape {
    final ArrayList<Shape> shapes;

    public Group(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public Group(Shape s1, Shape... sN){
        this.shapes = new ArrayList<>();
        this.shapes.add(s1);
        this.shapes.addAll(Arrays.asList(sN));
    }



    @Override
    public Hit intersect(Ray r) {
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
        return nearest;
    }
}
