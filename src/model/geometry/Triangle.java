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
        this.vNormal = UtilsMath.GetNormalFromTriangle(this, vNormal);
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
        this.vNormal = UtilsMath.GetNormalFromTriangle(this, vNormal);
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
        this.vNormal = UtilsMath.GetNormalFromTriangle(this, vNormal);
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
        this.vNormal = UtilsMath.GetNormalFromTriangle(this, vNormal);
        this.lightingValue = 0;
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
    public void editTranslate(float x, float y, float z, String vToUpdate) {
        if (vToUpdate.compareTo("vprocess") == 0) {
            this.vProcess[0].translate(x,y,z);
            this.vProcess[1].translate(x,y,z);
            this.vProcess[2].translate(x,y,z);
        } else if (vToUpdate.compareTo("vlist") == 0) {
            this.vList[0].translate(x,y,z);
            this.vList[1].translate(x,y,z);
            this.vList[2].translate(x,y,z);
        } else if (vToUpdate.compareTo("vprojection") == 0) {
            this.vProjection[0].translate(x,y,z);
            this.vProjection[1].translate(x,y,z);
            this.vProjection[2].translate(x,y,z);
        }
    }
    public void editRotateZ() {
        for (int i=0; i<3; i++)
            UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixZ);
    }
    public void editRotateX() {
        for (int i=0; i<3; i++)
            UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixX);
    }
    public void editRotateZFromVector(String vFromUpdate) {
        if (vFromUpdate.compareTo("vprocess") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixZ);
        else if (vFromUpdate.compareTo("vlist") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vList[i], this.vProcess[i], CameraModel.rotationMatrixZ);
        else if (vFromUpdate.compareTo("vprojection") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vProjection[i], this.vProcess[i], CameraModel.rotationMatrixZ);
    }
    public void editRotateXFromVector(String vFromUpdate) {
        if (vFromUpdate.compareTo("vprocess") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], CameraModel.rotationMatrixX);
        else if (vFromUpdate.compareTo("vlist") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vList[i], this.vProcess[i], CameraModel.rotationMatrixX);
        else if (vFromUpdate.compareTo("vprojection") == 0)
            for (int i=0; i<3; i++)
                UtilsMath.MultiplyMatrixVector(this.vProjection[i], this.vProcess[i], CameraModel.rotationMatrixX);
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
