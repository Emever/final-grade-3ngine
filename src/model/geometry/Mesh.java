package model.geometry;

import controller.EngineController;
import java.util.ArrayList;

/**
 *
 * @author Joel
 */
public class Mesh {
    private ArrayList<Triangle> tris;  // "polygons"
    private String name;
    
    public Mesh() {
        this.tris = new ArrayList<Triangle>();
        this.name = "";
    }
    public Mesh(String name) {
        this.tris = new ArrayList<Triangle>();
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Triangle> getTris() {
        return tris;
    }
    public void setTris(ArrayList<Triangle> tris) {
        this.tris = tris;
    }
    
    public void addTriangle(Triangle t) {
        this.tris.add(t);
    }
    public void addTriangle(Vertex v1, Vertex v2, Vertex v3) {
        this.tris.add(new Triangle(v1,v2,v3));
    }
    public void addTriangle(int id, Vertex v1, Vertex v2, Vertex v3) {
        this.tris.add(new Triangle(id,v1,v2,v3));
    }
    public Triangle getTriangle(int i) {
        return this.tris.get(i);
    }
    public void removeTriangle(int i) {
        this.tris.remove(i);
    }
    
    public int getMaxId() {
        int max = 0;
        for (Triangle t:this.tris)
            if (max <t.getId())
                max = t.getId();
        return max;
    }
    
    // PROJECTION METHOD
    public void calculateAllProjections() {
        for (Triangle t:this.tris) {
            // aqui hay un calculo que está "un poco mal"
            t.calculateTriangleProjection();
            t.scaleVertexToView();
        }
    }
    
    // TRANSLATING MESH
    public void editTranslate(float x, float y, float z) {
        for (Triangle t:this.tris)
            t.editTranslate(x,y,z);
    }
    // ROTATING MESH
    public void editRotateZ() {
        for (Triangle t:this.tris)
            t.editRotateZ();
    }
    public void editRotateX() {
        for (Triangle t:this.tris)
            t.editRotateX();
    }
}