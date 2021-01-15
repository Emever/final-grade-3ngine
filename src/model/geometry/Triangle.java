package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import utils.UtilsMath;

/**
 *
 * @author Joel
 */
public class Triangle {
    private int id;
    private Vertex[] vList;
    private Vertex[] vRotation;
    private Vertex[] vProjection;
    
    public Triangle() {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = new Vertex();
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vRotation = new Vertex[3];
    }
    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.id = 0;
        this.vList = new Vertex[3];
        this.vList[0] = v1;
        this.vList[1] = v2;
        this.vList[2] = v3;
        
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vRotation = new Vertex[3];
    }
    public Triangle(int id, Vertex v1, Vertex v2, Vertex v3) {
        this.id = id;
        this.vList = new Vertex[3];
        this.vList[0] = v1;
        this.vList[1] = v2;
        this.vList[2] = v3;
        
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vRotation = new Vertex[3];
    }
    public Triangle(int id, Vertex[] vs) {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = vs[i];
        
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vRotation = new Vertex[3];
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Vertex[] getvList() {
        return vList;
    }
    public void setvList(Vertex[] vList) {
        this.vList = vList;
    }
    public Vertex getVertex(int i) {
        if (i < 3 && i >= 0)
            return this.vList[i];
        System.out.println("Wrong requested index! (from triangle)");
        return null;
    }
    public Vertex getProjectionVertex(int i) {
        if (i < 3 && i >= 0)
            return this.vProjection[i];
        System.out.println("Wrong requested index! (from triangle)");
        return null;
    }
    public boolean setVertex(int i, Vertex v) {
        if (i < 3 && i >= 0) {
            this.vList[i] = v;
            return true;
        }
        return false;
    }
    public Vertex[] getvProjection() {
        return vProjection;
    }
    public void setvProjection(Vertex[] vProjection) {
        this.vProjection = vProjection;
    }

    public Vertex[] getvRotation() {
        return vRotation;
    }
    
    
    
    // PROJECTION METHODS
    
    public void calculateTriangleProjection() {
        for (int i=0; i<3; i++) {
            UtilsMath.CopyVertexValues(this.vList[i], this.vProjection[i]);
            UtilsMath.MultiplyMatrixVector(this.vList[i], this.vProjection[i], EngineController.projectionMatrix);
        }
    }
    public void calculateTriangleProjection(Vertex[] v) {
        for (int i=0; i<3; i++) {
            UtilsMath.MultiplyMatrixVector(v[i], this.vProjection[i], EngineController.projectionMatrix);
        }
    }
    
    public void scaleVertexToView() {
        for (int i=0; i<3; i++)
            this.vProjection[i].scaleToView();
        /*System.out.println("After scaling ____________");
        System.out.println(this.vProjection[0].toString());
        System.out.println(this.vProjection[1].toString());
        System.out.println(this.vProjection[2].toString());*/
    }
    
    
    // EDIT TRIANGLE METHODS
    public void editTranslate(float x, float y, float z) {
        this.vList[0].translate(x,y,z);
        this.vList[1].translate(x,y,z);
        this.vList[2].translate(x,y,z);
    }
    public void editRotateZ() {
        for (int i=0; i<3; i++) {
            this.vRotation[i] = new Vertex();
            UtilsMath.CopyVertexValues(this.vList[i], this.vRotation[i]);
            UtilsMath.MultiplyMatrixVector(this.vList[i], this.vRotation[i], EngineController.rotationMatrixZ);
        }
    }
    public void editRotateX() {
        for (int i=0; i<3; i++) {
            UtilsMath.CopyVertexValues(this.vList[i], this.vRotation[i]);
            UtilsMath.MultiplyMatrixVector(this.vList[i], this.vRotation[i], EngineController.rotationMatrixX);
        }
    }
}
