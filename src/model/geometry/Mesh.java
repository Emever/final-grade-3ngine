package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import utils.UtilsMath;

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
    
    // VPROCESS MANAGEMENT
    public void initTrianglesVProcess() {
        for (Triangle t:this.tris)
            t.initVProcess();
    }
    public void updateVListWithVProcess() {
        for (Triangle t:this.tris)
            t.updateVList();
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
    public void editRotate(String axis) {
        for (Triangle t:this.tris)
            t.editRotate(axis);
    }
    
    public void loadNormals() {
        for (Triangle t:this.tris)
            t.setVNormal(UtilsMath.GetNormalFromTriangle(t, null));
    }

    public void loadLightingValues() {
        float aux = 0;
        for (Triangle t:this.tris) {
            // we calculate the Dot Product of every T with the scene light
            aux = UtilsMath.DotProduct(t.getNormalVector(), EngineController.lightDirection);
            aux = (aux + 1f) / 2f;   // so we restrict lighting value from 0 to 1.
            t.setLightingValue(aux);
        }
    }

    public void loadDepthValues() {
        for (Triangle t:this.tris)
            t.calculateDepthValue();
    }
    
    public void sortTrianglesInDepth() {
        int loop = 0;
        while (loop < this.tris.size()-1) {
            for (int j=0; j<this.tris.size()-1; j++) {
                if (this.tris.get(j).getDepthValue() < this.tris.get(j+1).getDepthValue())
                {
                    Triangle aux = new Triangle(this.tris.get(j));
                    this.tris.set(j, this.tris.get(j+1));
                    this.tris.set(j+1, aux);
                }
            }
            loop++;
        }
    }
    
    
    public void delete() {
        for (Triangle t:this.tris) {
            t.delete();
        }
        this.tris = null;
    }
}
