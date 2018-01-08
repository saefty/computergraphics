package taher858897.a05;

import cgtools.Mat4;
import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.Material.GlassMaterial;
import taher858897.a05.Material.Material;
import taher858897.a05.Shape.*;
import taher858897.a05.Textures.TransformedPicTexture;

import java.io.IOException;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.*;
import static java.lang.Math.*;
import static java.lang.Math.PI;
import static taher858897.a05.Main.raytrace;
import static taher858897.a05.Main.rndColor;
import static taher858897.a05.Shape.Shape.EPSILON;

public class CompetitionScene {

    private static Material tableMaterial;
    private static Material fieldMaterial;
    private static Material face0;
    private static Material face1;
    private static Material face2;
    private static Material face3;
    private static Material skin;
    private static Material wallMaterial;
    static {
        try {
            skin = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/skin.jpg",
                    Mat4.scale(vec3(1.2))
                )
            );
            wallMaterial = new DiffuseMaterial(
                new TransformedPicTexture(
                    "texture/wallLogs.jpg",
                    Mat4.scale(vec3(3.5))
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

    private static Group genPerson(Material face, double headShift, double armDeg) throws IOException {
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
        head.setTransformation(Mat4.translate(0,0,headShift));
        Group underArm = new Group(
            new Cylinder(vec3(0,0,0), .35,1.2,skin),
            new Group(new Sphere(vec3(0,1.2,0), .6, skin), Mat4.scale(.4,1,1))
        );
        underArm.setTransformation(rotate(1,0,0,180-armDeg));
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
    }

    private static Group genPerson2(Material face, double headShift, double armDeg) throws IOException {
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
        head.setTransformation(Mat4.translate(0,-headShift,headShift/4));
        Group underArm = new Group(
                new Cylinder(vec3(0,0,0), .35,1.2,skin),
                new Group(new Sphere(vec3(0,1.2,0), .6, skin), Mat4.scale(.4,1,1))
        );
        underArm.setTransformation(rotate(1,0,0,180-armDeg));
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

    public static Group genScene(double frame) throws IOException {
        Group person = genPerson(face1, 1*(sin(frame*2/20.0)+1)*.5, 120*(sin(frame*2/30.0)+1)*.5);
        Group girl = genPerson2(face2, 1*(sin(frame*2/20.0)+1)*.5, 120*(sin(frame*2/30.0)+1)*.5);
        Group g = new Group(
            genField(),
            genTable(),
            genWalls(),
            genCards(20,1.2, frame >= 427 ? frame-427 : 0),
            new Group(person, Mat4.scale(vec3(.4)).multiply(translate(-2,-1.6,-2))),
            new Group(person, Mat4.scale(vec3(.4)).multiply(rotate(0,1,0,180)).multiply(translate(2,-1.6,-14))),
            new Group(girl, Mat4.scale(vec3(.4)).multiply(rotate(0,1,0,90)).multiply(translate(-5,-1.6,-7)))
        );
        //g.setTransformation(translate(2,-0.025,-2));
        return g;
    }

    private static Shape genCards(double count, double radius, double frame) throws IOException {
        Group cards = new Group();
        Vec3 p1 = vec3(-.25,0.00,-.7);
        Vec3 p2 = vec3(.25,.005,.7);
        Vec3 cur_pos1 = new Vec3(radius*sin(2*PI*2/count) , 0, radius*cos(2*PI*2/count) );
        Vec3 cur_pos2 = new Vec3(radius*sin(2*PI*2/count), 0, radius*cos(2*PI*2/count) );
        double deg = toDegrees(
                acos( (2*radius*radius-subtract( new Vec3(radius*sin(2*PI*(1)/count) + p1.x, p1.y, radius*cos(2*PI*(1)/count) + p1.z), cur_pos1).length())/(2*radius*radius)))
                ;
        double mutDeg = (deg + frame/10.0)%360;
        Material m = new DiffuseMaterial(
                new TransformedPicTexture(
                        "texture/redBack.jpg",
                        Mat4.identity
                )
        );
        Group rotationGroup = new Group();
        for (int i = 0; i < count; i++) {
            Group card = new Group(
                new Cube(p1, p2, m)
            );
            rotationGroup.addShape(card);
            card.setTransformation(Mat4.scale(vec3(.5)).multiply(rotate((sin(frame/50.0*2*PI-PI/2)+1)*.3,1,0, i*mutDeg)).multiply(
                translate(vec3(cos(toRadians(mutDeg))*(radius+(sin(frame/50.0*2*PI-PI/2)+1)*.25),0,sin(toRadians(mutDeg))*(radius+(sin(frame/50.0*2*PI-PI/2)+1)*.25)))
            ));
        }
        rotationGroup.setTransformation(rotate(0,1,0, frame).multiply(translate(0,(sin(frame/50.0*2*PI-PI/2)+1)*.3,0)));
        cards.addShape(rotationGroup);
        Group card = new Group(
                new Cube(p1, p2, new DiffuseMaterial(
                        new TransformedPicTexture(
                                "texture/king.png",
                                Mat4.identity
                        )
                ))
        );
        card.setTransformation(Mat4.scale(vec3(.5)).multiply(rotate(0,1,0, 4*deg)).multiply(
                translate(-.8,0,-2.5)
        ));
        cards.addShape(card);
        cards.addShapes(
                CSG.difference(
                    new Cylinder(vec3(0,0,0), .15,.5,new GlassMaterial(vec3(.8),1.5,0, false)),
                    new Cylinder(vec3(0,-EPSILON,0), .13,.5+EPSILON, new GlassMaterial(vec3(.9),1.5,0,false))
                ),
                new Cylinder(vec3(0,EPSILON,0), .128-EPSILON,.22+EPSILON, new DiffuseMaterial(vec3(.6,0,0)))

        );
        cards.setTransformation(translate(-1,.055,2));
        return cards;
    }

    private static Shape genWalls() throws IOException {
        Group walls = new Group(
            new Cube(vec3(-5,-1,-4), vec3(-5.2,4,1.0), wallMaterial),
            new Cube(vec3(-5,-1,3.5), vec3(-5.2,4,15), wallMaterial),
            new Cube(vec3(-5,-1,1.0), vec3(-5.2,.5,3.5), new DiffuseMaterial(new TransformedPicTexture("texture/wallLogs.jpg", Mat4.scale(vec3(1.27))))),
            new Cube(vec3(-5,2,1.0), vec3(-5.2,4,3.5), new DiffuseMaterial(new TransformedPicTexture("texture/wallLogs.jpg", Mat4.scale(vec3(1.23))))),
            new Cube(vec3(-5,.5,1.0), vec3(-5.2,2,3.5), new DiffuseMaterial(new TransformedPicTexture("texture/landscape.jpg", Mat4.scale(vec3(1)).multiply(rotate(1,0,0,180))))),
            new Cube(vec3(-5,-1,-4), vec3(5.2,4,-4.2), wallMaterial),
            new Cube(vec3(5,-1,-4), vec3(5.2,4,8), wallMaterial),
            new Cube(vec3(-5,-1,8), vec3(-4,4,8.2), wallMaterial),
            new Cube(vec3(-4,2,8), vec3(-2,4,8.2), wallMaterial),
            new Group(
                new Cube(vec3(0,0,0), vec3(-2+4,2+1,.2),
                    new DiffuseMaterial(
                        new TransformedPicTexture("texture/door.jpg", Mat4.rotate(1,0,0,180))
                    )
                ),
                Mat4.rotate(0,1,0,20).multiply(translate(-6.5,-1,6.8))
            ),
            new Cube(vec3(-2,-1,8), vec3(3,3,8.2), wallMaterial)
        );
        return walls;
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
