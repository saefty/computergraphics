package taher858897.a05.RayTracer;

import taher858897.a05.Shape.Group;

import java.io.Serializable;
import java.util.ArrayList;

public class World implements Serializable {
    public Group scene;
    public ArrayList<Light> lights;

    public World(Group scene, ArrayList<Light> lights){
        this.scene = scene;
        this.lights = lights;
    }
}
