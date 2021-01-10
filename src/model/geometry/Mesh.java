package model.geometry;

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
    
    public void calculateAllProjections(float[][] projectionMatrix) {
        for (Triangle t:this.tris) {
            t.calculateTriangleProjection(projectionMatrix);
            t.scaleVertexToView();
        }
    }
    
}
