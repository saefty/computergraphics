package taher858897.a05.Shape;

import cgtools.Vec3;
import taher858897.a05.Material.DiffuseMaterial;
import taher858897.a05.RayTracer.Hit;
import taher858897.a05.RayTracer.Ray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cgtools.Vec3.*;
import static java.lang.Math.abs;

public interface CSG extends Shape {
    class CSGUnion implements CSG {
    final Shape lhs;
    final Shape rhs;

    public CSGUnion(Shape lhs, Shape rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Hit intersect(Ray r) {
        Hit lhsHit = this.lhs.intersect(r);
        Hit rhsHit = this.rhs.intersect(r);
        if (lhsHit == null && rhsHit == null) return null;
        if (lhsHit == null) return rhsHit;
        if (rhsHit == null) return lhsHit;
        if (lhsHit.t < rhsHit.t) return lhsHit;
        if (lhsHit.t > rhsHit.t) return rhsHit;
        return null;
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox bb = lhs.bounds();
        bb = bb.extend(rhs.bounds());
        return bb;
    }

    @Override
    public boolean contains(Vec3 pos) {
        return false;
    }

        @Override
        public void loadTextures() throws IOException {
            lhs.loadTextures();
            rhs.loadTextures();
        }
    }

    class CSGDifference implements CSG{
    final Shape lhs;
    final Shape rhs;
    final CSGIntersection csgIntersection;

    public CSGDifference(Shape lhs, Shape rhs) {
        this.lhs = rhs;
        this.rhs = lhs;
        csgIntersection = new CSGIntersection(lhs, rhs);
    }

    @Override
    public Hit intersect(Ray r) {
        List<Hit> rhsHits = CSG.getHits(rhs, r.o, r.d, r.t0, r.t1,0);
        List<Hit> lhsHits = CSG.getHits(lhs, r.o, r.d, r.t0, r.t1,0);
        //if (lhsHits.isEmpty() || rhsHits.isEmpty()) return null;
        lhsHits = lhsHits.stream().filter(hit -> rhs.contains(hit.position)).collect(Collectors.toList());
        rhsHits = rhsHits.stream().filter(hit -> !lhs.contains(hit.position)).collect(Collectors.toList());
        Hit minLhs  = null;
        for (Hit h: lhsHits) {
            if (minLhs == null) minLhs = h;
            if (h.t < minLhs.t){
                minLhs = h;
            }
        }
        Hit minRhs  = null;
        for (Hit h: rhsHits) {
            if (minRhs == null) minRhs = h;
            if (h.t < minRhs.t){
                minRhs = h;
            }
        }
        if ((minLhs == null && minRhs != null)) return minRhs;
        if ((minRhs == null && minLhs != null)) return minLhs;
        if (minLhs != null && minLhs.t > minRhs.t) return minRhs;
        else return minLhs;
    }

    @Override
    public BoundingBox bounds() {
        return lhs.bounds();
    }

    @Override
    public boolean contains(Vec3 pos) {
        return false;
    }

        @Override
        public void loadTextures() throws IOException {
            lhs.loadTextures();
            rhs.loadTextures();
        }

    }

    class CSGIntersection implements CSG{
    final Shape lhs;
    final Shape rhs;

    private CSGIntersection(Shape lhs, Shape rhs) {
     this.lhs = lhs;
     this.rhs = rhs;
    }

    @Override
    public Hit intersect(Ray r) {
        List<Hit> rhsHits = CSG.getHits(rhs, r.o, r.d, r.t0, r.t1,0);
        List<Hit> lhsHits = CSG.getHits(lhs, r.o, r.d, r.t0, r.t1,0);
        if (lhsHits.isEmpty() || rhsHits.isEmpty()) return null;
        lhsHits = lhsHits.stream().filter(hit -> rhs.contains(hit.position)).collect(Collectors.toList());
        rhsHits = rhsHits.stream().filter(hit -> lhs.contains(hit.position)).collect(Collectors.toList());
        Hit minLhs  = null;
        for (Hit h: lhsHits) {
            if (minLhs == null) minLhs = h;
            if (h.t < minLhs.t){
                minLhs = h;
            }
        }
        Hit minRhs  = null;
        for (Hit h: rhsHits) {
            if (minRhs == null) minRhs = h;
            if (h.t < minRhs.t){
                minRhs = h;
            }
        }
        if ((minLhs == null && minRhs != null)) return minRhs;
        if ((minRhs == null && minLhs != null)) return minLhs;
        if (minLhs != null && minLhs.t > minRhs.t) return minRhs;
        else return minLhs;
    }

    @Override
    public BoundingBox bounds() {
        return lhs.bounds();
    }

     @Override
     public boolean contains(Vec3 pos) {
         return false;
     }

        @Override
        public void loadTextures() throws IOException {
            lhs.loadTextures();
            rhs.loadTextures();
        }

    }

    static CSG union(Shape lhs, Shape rhs){
    return new CSGUnion(lhs, rhs);
    }

    static ArrayList<Hit> getHits(Shape s, Vec3 o, Vec3 d, double tMin, double tMax, double tOffset){
    ArrayList<Hit> hits = new ArrayList<>();
    Hit h = s.intersect(
        new Ray(o, d, tMin, tMax)
    );
    if(h == null){
        return hits;
    }
    hits.addAll(getHits(s, h.position, d, EPSILON*100, tMax, h.t+tOffset+EPSILON));
    hits.add(h);
    h.t += tOffset;

    return hits;
    }

    static CSG difference(Shape lhs, Shape rhs){
    return new CSGDifference(lhs, rhs);
    }

    static CSG intersection(Shape lhs, Shape rhs){
    return new CSGIntersection(lhs, rhs);
    }
}
