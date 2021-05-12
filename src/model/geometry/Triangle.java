package model.geometry;

import controller.EngineController;
import java.util.ArrayList;
import static java.util.Objects.isNull;
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
    private Vertex[] vView;
    private Vertex[] vProjection;
    private Vertex vNormal;
    private float lightingValue = 0;
    private float depthValue;
    
    private boolean visible;
    
    // CONSTRUCTORS
    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.id = 0;
        this.vList = new Vertex[3];
        this.vList[0] = v1;
        this.vList[1] = v2;
        this.vList[2] = v3;
        
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProcess[i] = new Vertex(this.vList[i]);
        this.vView = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vView[i] = new Vertex(this.vList[i]);
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex(this.vList[i]);
        this.calculateVNormal();
        this.lightingValue = 0;
    }
    public Triangle(int id, Vertex v1, Vertex v2, Vertex v3) {
        this(v1,v2,v3);
        this.id = id;
    }
    public Triangle(Triangle source) {
        this.id = source.getId();
        this.lightingValue = source.getLightingValue();
        this.depthValue = source.getDepthValue();
        this.vNormal = new Vertex(source.getNormalVector());
        this.vList = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vList[i] = new Vertex(source.getVList(i));
        this.vProcess = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProcess[i] = new Vertex(source.getVProcess(i));
        this.vView = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vView[i] = new Vertex(source.getVView(i));
        this.vProjection = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vProjection[i] = new Vertex(source.getVProjection(i));
        this.visible = source.isVisible();
    }
    
    // GETTERS AND SETTERS
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
    public Vertex[] getVList() {
        return vList;
    }
    public void setVList(Vertex[] vList) {
        this.vList = vList;
    }
    public Vertex getVList(int i) {
        if (i < 3 && i >= 0)
            return this.vList[i];
        System.out.println("Wrong requested index! (from triangle)");
        return null;
    }
    public void setVList(Vertex newVertex, int index) {
        this.vList[index] = newVertex;
    }
    public Vertex[] getVProcess() {
        return vProcess;
    }
    public Vertex getVProcess(int i) {
        return this.vProcess[i];
    }
    public void setvProcess(Vertex[] vProcess) {
        this.vProcess = vProcess;
    }
    public void setVProcess(Vertex newVertex, int index) {
        this.vProcess[index] = newVertex;
    }
    public Vertex[] getVView() {
        return vView;
    }
    public Vertex getVView(int i) {
        return this.vView[i];
    }
    public void setVView(Vertex newVertex, int index) {
        this.vView[index] = newVertex;
    }
    public void setVView(Vertex[] vView) {
        this.vView = vView;
    }
    public Vertex[] getVProjection() {
        return vProjection;
    }
    public Vertex getVProjection(int i) {
        return this.vProjection[i];
    }
    public void setVProjection(Vertex[] vProjection) {
        this.vProjection = vProjection;
    }
    public void setVProjection(Vertex vProjection, int index) {
        this.vProjection[index] = vProjection;
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
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    // TRIANGLE ATTRIBUTES' CHECKING AND CALCULATIONS
    public void checkIfBehindCamera() {
        //boolean view = this.vNormal.getZ() < 0;
        this.visible = Math.max(
                this.vProjection[0].getZ(),
                Math.max(
                        this.vProjection[1].getZ(),
                        this.vProjection[2].getZ()
                    )
            ) >= 0f;
    }
    public void checkIfFacingCamera() {
        Vertex cameraRay = UtilsMath.SubVertex(this.vProcess[0], EngineController.camera.getPos());
        //System.out.println("cameraRay" + cameraRay.toString());
        this.visible = UtilsMath.DotProduct(this.vNormal, cameraRay) < 0f;
        //System.out.println("camVDir: " + EngineController.camera.getvDir().toString());
        //System.out.println("vNormal: " + this.vNormal.toString() + " -> dot product: " + String.format("%.2f", dotProduct));
        //System.out.println("________________________________\n");
    }
    public void calculateDepthValue() {
        /*      
        // median value
        float x = (this.vProcess[0].getX() + 
                this.vProcess[1].getX() +
                this.vProcess[2].getX()) / 3;
        float y = (this.vProcess[0].getY() + 
                this.vProcess[1].getY() +
                this.vProcess[2].getY()) / 3;
        float z = (this.vProcess[0].getZ() + 
                this.vProcess[1].getZ() +
                this.vProcess[2].getZ()) / 3;
        Vertex vToCamera = new Vertex(x,y,z);
        this.depthValue = UtilsMath.SubVertex(EngineController.camera.getPos(), vToCamera).getLength();
        */
        this.depthValue =(this.vProjection[0].getZ() + 
                this.vProjection[1].getZ() +
                this.vProjection[2].getZ()) / 3;
        
        // value closer to the camera
        /*
        this.depthValue =
                Math.min(
                    Math.min(
                        this.vProjection[0].getZ(), 
                        this.vProjection[1].getZ()),
                this.vProjection[2].getZ());
        */
    }
    public void calculateVNormal() {
        // we need 2 vectors with the same origin vertex: (1)
        Vertex vector1 = UtilsMath.SubVertex(this.vProcess[1], this.vProcess[0]);
        // we need 2 vectors with the same origin vertex: (2)
        Vertex vector2 = UtilsMath.SubVertex(this.vProcess[2], this.vProcess[0]);
        // calculate normal vector
        this.vNormal = UtilsMath.CrossProduct(vector1, vector2, null);
        // we normalize the vector
        this.vNormal.normalize();
        
    }
    public void scaleVertexToView() {
        for (int i=0; i<3; i++)
            this.vProcess[i].scaleToView();
    }
    
    // EDIT TRIANGLE METHODS
    public void translate(float x, float y, float z) {
        this.vProcess[0].translate(x,y,z);
        this.vProcess[1].translate(x,y,z);
        this.vProcess[2].translate(x,y,z);
    }
    public void rotate(String axis, float[][] matrix) {
        axis = axis.toUpperCase();

        switch(axis) {
            case "X":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], matrix);
                break;
            case "Y":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], matrix);
                break;
            case "Z":
                for (int i=0; i<3; i++)
                    UtilsMath.MultiplyMatrixVector(this.vProcess[i], this.vProcess[i], matrix);
                break;
        }
    }
    public void calculateVertexTransformation(float[][] matrix) {
       for (int i=0; i<3; i++) {
           Vertex vAux = new Vertex();
           UtilsMath.MultiplyMatrixVector(this.vProcess[i], vAux, matrix);
           UtilsMath.CopyVertexValues(vAux, this.vProcess[i]);
       }
    }
    public void calculateLightingValue() {
        // we calculate the Dot Product of every T with the scene light
        this.lightingValue = UtilsMath.DotProduct(this.vNormal, EngineController.lightDirection);
        this.lightingValue = (this.lightingValue - 1f) / 2f;   // so we restrict lighting value from -1 to 0.
        this.lightingValue = Math.abs(this.lightingValue);
        this.lightingValue = Math.max(.1f, this.lightingValue);
    }
    
    // DANGER ZONE
    public void delete() {
        if (!isNull(this.vProjection))
            for (Vertex v:this.vProjection)
                v.delete();
        if (!isNull(this.vView))
            for (Vertex v:this.vView)
                v.delete();
        if (!isNull(this.vProcess))
            for (Vertex v:this.vProcess)
                v.delete();
        if (!isNull(this.vList))
            for (Vertex v:this.vList)
                v.delete();
        this.lightingValue = 0;
        this.vProjection = null;
        this.vView = null;
        this.vProcess = null;
        this.vList = null;
        this.id = 0;
    }
}
