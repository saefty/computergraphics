package taher858897.a05;

import cgtools.Mat4;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.GlassMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.Shape.*;
import taher858897.a05.Textures.TransformedPicTexture;

import java.io.IOException;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.black;
import static cgtools.Vec3.red;
import static cgtools.Vec3.vec3;
import static taher858897.a05.Main.raytrace;
import static taher858897.a05.Main.rndColor;

public class CompetitionScene {

    private static Material tableMaterial;
    private static Material fieldMaterial;
    private static Material face0;
    private static Material face1;
    private static Material face2;
    private static Material face3;
    private static Material skin;
    static {
        try {
            skin = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/skin.jpg",
                    Mat4.scale(vec3(1.2))
                )
            );
            face0 = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/face0.jpg",
                    Mat4.scale(vec3(1.8,1.4,1)).multiply(translate(.90,0,0))
                )
            );
            face1 = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/face1.jpg",
                    Mat4.scale(vec3(1.8,1.4,1)).multiply(translate(.90,-.1,0))
                )
            );
            face2 = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/face2.jpg",
                    Mat4.scale(vec3(1.5,1.1,1)).multiply(translate(-.165,-.06,0))
                )
            );
            face3 = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/face3.jpg",
                    Mat4.scale(vec3(1.8,1.4,1)).multiply(translate(.90,-.1,0))
                )
            );
            fieldMaterial = new GlassMaterial(vec3(.3),1.5,0);
            tableMaterial = new DiffuseMaterial(new TransformedPicTexture("texture/wood.jpg", Mat4.scale(3,3,3)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Group genPerson(Material face) throws IOException {
        Group g = new Group();
        Group head = new Group(
                new Sphere(vec3(0,0,0),.8, face),
                new Group(
                        new Cone(vec3(0,0,0),.85,40, new DiffuseMaterial(
                                new TransformedPicTexture("texture/partyHat.jpg", Mat4.scale(vec3(.34)))
                        )),
                        rotate(0,0,1,180).multiply(Mat4.translate(0,-1.4,0))
                )
        );
        Group underArm = new Group(
            new Cylinder(vec3(0,0,0), .35,1.2,skin),
            new Group(new Sphere(vec3(0,1.2,0), .6, skin), Mat4.scale(.4,1,1))
        );
        underArm.setTransformation(rotate(1,0,0,180));
        Group arm = new Group(
            new Sphere(vec3(0,0,0), .45, skin),
            new Cylinder(vec3(0,0,0),.35,1.3, skin),
            underArm
        );
        Group leArm = new Group(
            new Sphere(vec3(.25,1.3,0), .50, skin),
            arm
        );
        leArm.setTransformation(translate(-1.8,-2.2,0));
        Group reArm = new Group(
            new Sphere(vec3(-.25,1.3,0), .50, skin),
            arm
        );
        reArm.setTransformation(translate(1.8,-2.2,0));
        Group arms = new Group(
            leArm,
            reArm
        );
        Group body = new Group(
            new Cube(vec3(-1.1,-1.5,-.8), vec3(1.1,-.65,.45), skin),
            new Group(
                new Sphere(vec3(0,-2.5,0), 2,skin),
                Mat4.scale(.68,.75,.39)
            ),
            arms,
            head
        );

        Group legs = new Group(
                new Sphere(vec3(-.8,2,0),.55, skin),
                new Sphere(vec3(.8,2,0),.55, skin),
                new Group(new Sphere(vec3(-.8,-2.2,.2), .6, skin), Mat4.scale(1,.4,1)),
                new Group(new Sphere(vec3(.8,-2.2,.2), .6, skin), Mat4.scale(1,.4,1)),
                new Cylinder(vec3(-.8,-1,0),.45,3, skin),
                new Cylinder(vec3(.8,-1,0),.45,3, skin)
        );
        body.setTransformation(translate(0,5,0));
        g.addShapes(
            legs,
            body
        );
        return g;
        /*

         */
    }

    public static Group genScene() throws IOException {
        Group g = new Group(
            genField(),
            genTable(),
            new Group(genPerson(face1), Mat4.scale(vec3(.4)).multiply(translate(-2,-1.6,-2))),
            new Group(genPerson(face2), Mat4.scale(vec3(.4)).multiply(rotate(0,1,0,180)).multiply(translate(2,-1.6,-12)))
        );
        //g.setTransformation(translate(2,-0.025,-2));
        return g;
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

    private static Group genField(){
        return new Group(
            new Cube(vec3(0,0,0), vec3(-2,0.05,4), fieldMaterial)
        );
    }
}
