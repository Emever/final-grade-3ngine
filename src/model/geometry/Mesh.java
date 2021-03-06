package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import java.util.Iterator;
import static java.util.Objects.isNull;
import utils.UtilsMath;

/**
 *
 * @author Joel
 */
public class Mesh {
    private String name;
    private Vertex rot; // rotation in radians
    private Vertex addToRot;
    private Vertex pos; // origin vertex position
    private Vertex addToPos;
    private ArrayList<Triangle> tris;  // "polygons"
    
    public Mesh(String name, Vertex initPos) {
        this.tris = new ArrayList<Triangle>();
        this.name = name;
        
        this.pos = (isNull(initPos))? new Vertex(0.0f, 0.0f, 0.0f) : new Vertex(initPos);
        this.addToPos = new Vertex(0.0f, 0.0f, 0.0f);
        
        this.rot = new Vertex(0.0f, 0.0f, 0.0f);
        //this.addToRot = new Vertex(UtilsMath.DegToRads(45), UtilsMath.DegToRads(5), -UtilsMath.DegToRads(15));
        this.addToRot = new Vertex(0f, UtilsMath.DegToRads(45), 0f);
        //this.addToRot = new Vertex(UtilsMath.DegToRads(45), 0f, 0f);
        //this.addToRot = new Vertex(0f, 0f, UtilsMath.DegToRads(45));
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

    public void loadDepthValues() {
        for (Triangle t:this.tris)
            t.calculateDepthValue();
    }

    public void sortTrianglesByDepth() {
        /*     
        int loop = 0;
        while (loop < this.tris.size()-1) {
            for (int j=0; j<this.tris.size()-2; j++)
                if (this.tris.get(j).getDepthValue() < this.tris.get(j+1).getDepthValue())
                {
                    // option 1
                    Triangle aux = this.tris.get(j);
                    this.tris.set(j, this.tris.get(j+1));
                    this.tris.set(j+1, aux);
                    
                    //option 2: no flickering
                    //this.tris.add(j, this.tris.get(j+1));
                    //this.tris.remove(j+2); 
                }
            loop++;
        }
        */

        int i0 = 0;
        while (i0 < this.tris.size()-2) {
            if (this.tris.get(i0).isVisible()) {
                int maxLocation = i0;
                float max = this.tris.get(maxLocation).getDepthValue();
                for (int iF=i0+1; iF<this.tris.size()-1; iF++)
                    if (this.tris.get(iF).isVisible())
                        if (max < this.tris.get(iF).getDepthValue()) {
                            maxLocation = iF;
                            max = this.tris.get(iF).getDepthValue();
                        }
                // SWAP!
                Triangle auxT = this.tris.get(i0);
                this.tris.set(i0, this.tris.get(maxLocation));
                this.tris.set(maxLocation, auxT);
            }
            i0++;
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
