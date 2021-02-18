package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import model.CameraModel;
import utils.UtilsMath;

/**
 *
 * @author Joel
 */
public class Triangle {
    private int id;
    private Vertex[] vList;
    private Vertex[] vProcess;
    private Vertex[] vProjection;
    private Vertex vNormal;
    private float lightingValue = 0;
    private float depthValue;
    
    public Triangle() {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = new Vertex();
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++) {
            this.vProcess[i] = new Vertex();
            UtilsMath.CopyVertexValues(this.vList[i], this.vProcess[i]);
        }
        this.calculateVNormal();
        this.lightingValue = 0;
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
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++) {
            this.vProcess[i] = new Vertex();
            UtilsMath.CopyVertexValues(this.vList[i], this.vProcess[i]);
        }
        this.calculateVNormal();
        this.lightingValue = 0;
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
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++) {
            this.vProcess[i] = new Vertex();
            UtilsMath.CopyVertexValues(this.vList[i], this.vProcess[i]);
        }
        this.calculateVNormal();
        this.lightingValue = 0;
    }
    public Triangle(int id, Vertex[] vs) {
        this.id = 0;
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = vs[i];
        
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex();
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++) {
            this.vProcess[i] = new Vertex();
            UtilsMath.CopyVertexValues(this.vList[i], this.vProcess[i]);
        }
        this.calculateVNormal();
        this.lightingValue = 0;
    }
    public Triangle(Triangle source) {
        this.id = source.getId();
        this.lightingValue = source.getLightingValue();
        this.depthValue = source.getDepthValue();
        this.vNormal = new Vertex(source.getNormalVector());
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = new Vertex(source.getVertex(i));
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex(source.getProjectionVertex(i));
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++) {
            this.vProcess[i] = new Vertex(source.getVProcess()[i]);
        }
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setVNormal(Vertex n) {
        this.vNormal = n;
    }
    public Vertex getNormalVector() {
        return this.vNormal;
    }
    public float getLightingValue() {
        return lightingValue;
    }
    public void setLightingValue(float lightingValue) {
        this.lightingValue = lightingValue;
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
    public Vertex[] getVProcess() {
        return vProcess;
    }
    public Vertex[] getvProcess() {
        return vProcess;
    }
    public void setvProcess(Vertex[] vProcess) {
        this.vProcess = vProcess;
    }
    public Vertex getvNormal() {
        return vNormal;
    }
    public void setvNormal(Vertex vNormal) {
        this.vNormal = vNormal;
    }
    public float getDepthValue() {
        return depthValue;
    }
    public void setDepthValue(float depthValue) {
        this.depthValue = depthValue;
    }
    
    
    public boolean isVisible() {
        //boolean view = this.vNormal.getZ() < 0;
        boolean view =
            this.vNormal.getX() * (this.vProcess[0].getX() - EngineController.camera.getPos().getX()) +
            this.vNormal.getY() * (this.vProcess[0].getY() - EngineController.camera.getPos().getY()) +
            this.vNormal.getZ() * (this.vProcess[0].getZ() - EngineController.camera.getPos().getZ()) < 0;
        return view;
    }
    
    
    // VPROCESS MANAGEMENT:
    public void initVProcess() {    // we initialize VProcess vertexes
        for (int i=0; i<3; i++) {
            UtilsMath.CopyVertexValues(this.vList[i], this.vProcess[i]);
            UtilsMath.CopyVertexValues(this.vList[i], this.vProjection[i]);
        }
    }
    public void updateVList() {
        for (int i=0; i<3; i++)
            UtilsMath.CopyVertexValues(this.vProcess[i], this.vList[i]);
    }
    
    // PROJECTION METHODS
    public void calculateTriangleProjection() {
        for (int i=0; i<3; i++) {
            UtilsMath.CopyVertexValues(this.vProcess[i], this.vProjection[i]);
            UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProjection[i], CameraModel.projectionMatrix);
        }
    }
    public void calculateTriangleProjection(Vertex[] v) {
        for (int i=0; i<3; i++) {
            UtilsMath.MultiplyMatrixVector(v[i], this.vProjection[i], CameraModel.projectionMatrix);
        }
    }
    public void calculateDepthValue() {
        this.depthValue =
                (this.vProcess[0].getZ() + 
                this.vProcess[1].getZ() +
                this.vProcess[2].getZ()) / 3;
    }
    public void calculateVNormal() {
        // we need 2 vectors with the same origin vertex: (1)
        Vertex v1 = UtilsMath.SubVertex(this.vProcess[1], this.vProcess[0]);
        // we need 2 vectors with the same origin vertex: (2)
        Vertex v2 = UtilsMath.SubVertex(this.vProcess[2], this.vProcess[0]);
        // calculate normal vector
        this.vNormal = UtilsMath.CrossProduct(v1, v2, this.vNormal);
        // we normalize the vector
        this.vNormal.normalize();
    }
    public void scaleVertexToView() {
        for (int i=0; i<3; i++)
            this.vProjection[i].scaleToView();
    }
    
    
    // EDIT TRIANGLE METHODS
    public void editTranslate(float x, float y, float z) {
        this.vProcess[0].translate(x,y,z);
        this.vProcess[1].translate(x,y,z);
        this.vProcess[2].translate(x,y,z);
    }
    public void editRotate(String axis) {
        axis = axis.toUpperCase();

        switch(axis) {
            case "X":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixX);
                break;
            case "Y":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixY);
                break;
            case "Z":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixZ);
                break;
        }
    }

    public void delete() {
        for (Vertex v:this.vProjection)
            v.delete();
        for (Vertex v:this.vProcess)
            v.delete();
        for (Vertex v:this.vList)
            v.delete();
        this.lightingValue = 0;
        this.vProjection = null;
        this.vProcess = null;
        this.vList = null;
        this.id = 0;
    }
}
