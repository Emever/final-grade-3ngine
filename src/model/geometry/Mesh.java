package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import static java.util.Objects.isNull;
import utils.UtilsMath;

/**
 *
 * @author Joel
 */
public class Mesh {
    private ArrayList<Triangle> tris;  // "polygons"
    private Vertex rot; // rotation in radians
    private Vertex addToRot;
    private Vertex pos; // origin vertex position
    private Vertex addToPos;
    
    private String name;
    
    public Mesh(String name, Vertex initPos) {
        this.tris = new ArrayList<Triangle>();
        this.name = name;
        
        this.pos = (isNull(initPos))? new Vertex(0.0f,0.0f,0.0f) : new Vertex(initPos);
        this.addToPos = new Vertex(0.0f,0.5f,0.0f);
        
        this.rot = new Vertex(0.0f,0.0f,0.0f);
        this.addToRot = new Vertex(0.0f,UtilsMath.DegToRads(45),0.0f);
        
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
    public Vertex getRot() {
        return rot;
    }
    public void setRot(Vertex rot) {
        this.rot = rot;
    }
    public Vertex getAddToRot() {
        return addToRot;
    }
    public void setAddToRot(Vertex addToRot) {
        this.addToRot = addToRot;
    }
    public Vertex getPos() {
        return pos;
    }
    public void setPos(Vertex pos) {
        this.pos = pos;
    }
    public Vertex getAddToPos() {
        return addToPos;
    }
    public void setAddToPos(Vertex addToPos) {
        this.addToPos = addToPos;
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
    
    
    // TRANSLATING MESH
    public void editTranslate(float x, float y, float z) {
        for (Triangle t:this.tris)
            t.editTranslate(x,y,z);
    }
    
    // ROTATING MESH
    public void editRotate(String axis, float[][] matrix) {
        for (Triangle t:this.tris)
            t.editRotate(axis, matrix);
    }
    
    public void loadNormals() {
        for (Triangle t:this.tris)
            t.calculateVNormal();
    }

    public void loadDepthValues() {
        for (Triangle t:this.tris)
            t.calculateDepthValue();
    }
    
    public void sortTrianglesInDepth() {
        for (Triangle t:this.tris)
            t.calculateDepthValueRegarding("projection");
        
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

    public void loadTransformations(float[][] matrix) {
        for (Triangle t:this.tris) {
            t.calculateVertexTransformation(matrix);
        }
    }
    
    public void loadLightingValues() {
        for (Triangle t:this.tris) {
            t.calculateLightingValue();
        }
    }
    
    
    
    
    
    
    
    public void delete() {
        for (Triangle t:this.tris) {
            t.delete();
        }
        this.tris = null;
    }
}
