package model.geometry;

import java.util.ArrayList;
import utils.UtilsMath;

/**
 *
 * @author Joel
 */
public class Triangle {
    private int id;
    private Vertex[] vList;
    private Vertex[] vProjection;
    
    public Triangle() {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = new Vertex();
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
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
    }
    public Triangle(int id, Vertex[] vs) {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = vs[i];
        
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
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
    
    public void calculateTriangleProjection(float[][] m) {
        this.vProjection[0] = new Vertex();
        this.vProjection[1] = new Vertex();
        this.vProjection[2] = new Vertex();
        UtilsMath.CopyVertexValues(this.vList[0], this.vProjection[0]);
        UtilsMath.CopyVertexValues(this.vList[1], this.vProjection[1]);
        UtilsMath.CopyVertexValues(this.vList[2], this.vProjection[2]);
        System.out.println("Before projection ____________");
        System.out.println(this.vProjection[0].toString());
        System.out.println(this.vProjection[1].toString());
        System.out.println(this.vProjection[2].toString());
        UtilsMath.MultiplyMatrixVector(this.vList[0], this.vProjection[0], m);
        UtilsMath.MultiplyMatrixVector(this.vList[1], this.vProjection[1], m);
        UtilsMath.MultiplyMatrixVector(this.vList[2], this.vProjection[2], m);
        System.out.println("After projection ____________");
        System.out.println(this.vProjection[0].toString());
        System.out.println(this.vProjection[1].toString());
        System.out.println(this.vProjection[2].toString());
    }
    
    public void scaleVertexToView() {
        this.vProjection[0].scaleToView();
        this.vProjection[1].scaleToView();
        this.vProjection[2].scaleToView();
        System.out.println("After scaling ____________");
        System.out.println(this.vProjection[0].toString());
        System.out.println(this.vProjection[1].toString());
        System.out.println(this.vProjection[2].toString());
    }
}
