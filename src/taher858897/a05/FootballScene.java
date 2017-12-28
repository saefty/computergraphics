package taher858897.a05;

import cgtools.ImageTexture;
import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.GlassMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.Material.ReflectionMaterial;
import taher858897.a05.Shape.*;
import taher858897.a05.Textures.ConstantTexture;
import taher858897.a05.Textures.PicTexture;
import taher858897.a05.Textures.TransformedPicTexture;

import java.io.IOException;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.*;

public class FootballScene {

    private static Material goalMaterial = new ReflectionMaterial(vec3(.7),.1);
    private static Material tableMaterial = new ReflectionMaterial(vec3(86/255.0,47/255.0 ,14/255.0),0.3);
    private static Material ballMaterial;
    private static Material fieldMaterial;
    static {
        try {
            fieldMaterial = new DiffuseMaterial(new TransformedPicTexture("texture/gras.jpg", Mat4.scale(3,3,3)));
            ballMaterial = new DiffuseMaterial(new TransformedPicTexture("texture/ball.png", Mat4.scale(3,3,3)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Material ballMaterial1 = ballMaterial;
    private static Material ballMaterial2 = ballMaterial;

    private static Material holderMaterial = new DiffuseMaterial(new ConstantTexture(vec3(.05)));

    private static Material pointMaterial1 = new GlassMaterial(vec3(.1,.1,.8),1.5,0);
    private static Material pointMaterial2 = new GlassMaterial(vec3(.8,.2,.2),1.5,0);

    public static Group genScene(){
        Group backMesh = genMeshWall(4,4, vec3(1));
        backMesh.setTransformation(Mat4.scale(2.5,1,1));
        Group g = new Group(
            genField(),
            genTable(),
            genPointCubes(pointMaterial1),
            new Group(genPointCubes(pointMaterial2), translate(0,0,2.6)),
            new Group(
                new Sphere(vec3(-.5,.15,2),0.1, ballMaterial),
                new Sphere(vec3(-.9,.15,1.5),0.1, ballMaterial1),
                new Sphere(vec3(-1.2,.15,2.5),0.1, ballMaterial2)
            ),
            new Group(
                new Group(genGoal(backMesh), rotate(0,1,0,180).multiply(translate(1,0.025,-.5))),
                new Group(genGoal(backMesh), rotate(0,1,0,0).multiply(translate(-1,0.025,3.5)))
            )
        );
        g.setTransformation(translate(2,-0.025,-2));
        return g;
    }

    private static Group genPointCubes(Material pointMaterial) {
        Cube cube = new Cube(vec3(0,.05,0), vec3(-.05,.2,.1), pointMaterial);
        Group c = new Group (
            new Cylinder(vec3(0),.025,1.3,holderMaterial),
            new Cube(vec3(-.05,1.25,-.05),vec3(.05,1.3,.1), holderMaterial),
            new Cube(vec3(-.05,0,-.05),vec3(.05,0.05,.1), holderMaterial)
        );
        return new Group(
            new Group(cube, Mat4.translate(.1,0,0)),
            new Group(cube, Mat4.translate(.1,0,.2)),
            new Group(cube, Mat4.translate(.1,0,.4)),
            new Group(cube, Mat4.translate(.1,0,.8)),
            new Group(cube, Mat4.translate(.1,0,1)),
            new Group(c, Mat4.rotate(1,0,0,90).multiply(translate(.05,-.1,-.1)))
        );
    }

    private static Group genTable() {
        Shape feet = new Cube(vec3(0,-1,0), vec3(.3,0.025,.4), tableMaterial);
        Group feets = new Group(
            new Group(feet, Mat4.translate(-.2,0,0)),
            new Group(feet, Mat4.translate(-2.05,0,0)),
            new Group(feet, Mat4.translate(-.2,0,3.6)),
            new Group(feet, Mat4.translate(-2.05,0,3.6))
        );
        Group bars = new Group(
           new Cube(vec3(.2,-0.15,-0.15), vec3(-2.08,0.025,4.15), tableMaterial)
        );
        Group res = new Group(
            feets,
            bars
        );
        return res;
    }

    public static Group genField(){
        return new Group(
            new Cube(vec3(0,0,0), vec3(-2,0.05,4), fieldMaterial)
        );
    }

    public static Group genGoal(Group backMesh){
        Group top = new Group(
            new Cylinder(vec3(.35,0,0),.02,.73, goalMaterial)
        );
        top.setTransformation(translate(vec3(.362,-.062,0)).multiply(rotate(vec3(0,0,1),90)));
        Group mesh = new Group(
            new Group(backMesh, Mat4.scale(1.3,1.2,1)),
            new Group(backMesh, rotate(-1,0,0,90).multiply(translate(0,-.1,.42)).multiply(Mat4.scale(1.3,1,1))),
            new Group (
                new Group(backMesh, Mat4.identity.multiply(rotate(0,1,0,90)).multiply(translate(.05,.025,.05+.6))),
                Mat4.scale(1,1.1,.46)
            ),
            new Group (
                new Group(backMesh, Mat4.identity.multiply(rotate(0,-1,0,90)).multiply(translate(-.43,.025,.05))),
                Mat4.scale(1,1.1,.5)
            )
        );
        mesh.setTransformation(translate(vec3(-.3,-.12,.25)));

        Group g = new Group(
            top,
            new Cylinder(vec3(.35,0,0),.02,.3, goalMaterial),
            new Cylinder(vec3(-.35,0,0),.02,.3, goalMaterial),
            mesh
        );
        return g;
    }
    public static Group genMeshWall(int cols, int rows, Vec3 scale){
        Group res = new Group();
        Shape meshElement = genMeshElement(vec3(0,0,0), 1);
        for (int i = 0; i < cols; i++) {
            Group colRes = new Group();
            for (int j = 0; j < rows; j++) {
                colRes.addShape(new Group(meshElement, translate(i/15.0,j/15.0,0)));
            }
            res.addShape(colRes);
        }
        res.setTransformation(Mat4.scale(scale));
        return res;
    }

    public static Group genMeshElement(Vec3 pos, double scale){
        Group parallels = new Group(
            new Cylinder(vec3(-.028,.1,0),.005,.040, goalMaterial),
            new Cylinder(vec3(.028,.1,0),.005,.040, goalMaterial)
        );

        Group res = new Group(
            parallels,
            new Group(parallels, translate(vec3(-.096,.0535,0)).multiply(rotate(vec3(0,0,-1),55))),
            new Group(parallels, translate(vec3(.096,.0535,0)).multiply(rotate(vec3(0,0,-1),-55)))
        );
        res.setTransformation(Mat4.scale(scale,scale,scale).multiply(translate(pos)));
        return res;
    }
}
