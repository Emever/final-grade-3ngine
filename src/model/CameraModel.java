package model;

import controller.EngineController;
import model.geometry.Vertex;
import utils.UtilsMath;

/**
 * CameraModel handles all the translating, rotation, projection and scaling
 * of the scene objects into the EngineView.
 * 
 * @author Joel
 */
public class CameraModel {
    private EngineController engine;
    
    public Vertex pos;
    public float fNear, fFar, fFOV, fAspectRatio;
    
    //public static float fTheta;
    public static float[][] projectionMatrix;
    public static float[][] rotationMatrixX, rotationMatrixZ, rotationMatrixY;
    
    public CameraModel(EngineController controller) {
        this.engine = controller;
        this.pos = new Vertex(0,0,0, "Camera position");
        
        this.fAspectRatio = (float)EngineModel.dimY / (float)EngineModel.dimX;
    }
    public CameraModel(EngineController controller, float[] initValues) {
        this.engine = controller;
        this.pos = new Vertex(0,0,0, "Camera position");
        this.fNear = initValues[1];
        this.fFar = initValues[2];
        this.fFOV = initValues[0];
        
        this.fAspectRatio = (float)EngineModel.dimY / (float)EngineModel.dimX;
    }

    public Vertex getPos() {
        return pos;
    }
    public void setPos(Vertex pos) {
        this.pos = pos;
    }
    public float getfNear() {
        return fNear;
    }
    public void setfNear(float fNear) {
        this.fNear = fNear;
    }
    public float getfFar() {
        return fFar;
    }
    public void setfFar(float fFar) {
        this.fFar = fFar;
    }
    public float getfFOV() {
        return fFOV;
    }
    public void setfFOV(float fFOV) {
        this.fFOV = fFOV;
    }
    
    
    public void init() {
        this.createProjectionMatrix();
        this.createRotationMatrixX(EngineController.fTheta);
        this.createRotationMatrixY(EngineController.fTheta);
        this.createRotationMatrixZ(EngineController.fTheta);
    }
    
    public void createProjectionMatrix() {
        // create the projection matrix (it won't change whilst running)
        CameraModel.projectionMatrix = UtilsMath.getProjectionMatrix(fFOV, fAspectRatio, fNear, fFar);
    }
    
    public void createRotationMatrixX(float theta) {
        CameraModel.rotationMatrixX = UtilsMath.getRotationMatrix_X(theta);
    }
    public void createRotationMatrixY(float theta) {
        CameraModel.rotationMatrixY = UtilsMath.getRotationMatrix_Y(theta);
    }
    public void createRotationMatrixZ(float theta) {
        CameraModel.rotationMatrixZ = UtilsMath.getRotationMatrix_Z(theta);
    }
    
    public void updateRotationMatrixes(float rotX, float rotY, float rotZ) {
        // Z Rotation Matrix
        CameraModel.rotationMatrixZ[0][0] = (float)Math.cos(rotZ);
        CameraModel.rotationMatrixZ[1][1] = (float)Math.cos(rotZ);
        CameraModel.rotationMatrixZ[0][1] = (float)Math.sin(rotZ);
        CameraModel.rotationMatrixZ[1][0] = -(float)Math.sin(rotZ);
        // X Rotation Matrix
        CameraModel.rotationMatrixX[1][1] = (float)Math.cos(rotX);
        CameraModel.rotationMatrixX[2][2] = (float)Math.cos(rotX);
        CameraModel.rotationMatrixX[1][2] = (float)Math.sin(rotX);
        CameraModel.rotationMatrixX[2][1] = -(float)Math.sin(rotX);
        // Y Rotation Matrix
        CameraModel.rotationMatrixY[0][0] = (float)Math.cos(rotY);
        CameraModel.rotationMatrixY[2][2] = (float)Math.cos(rotY);
        CameraModel.rotationMatrixY[2][0] = (float)Math.sin(rotY);
        CameraModel.rotationMatrixY[0][2] = -(float)Math.sin(rotY);
    }
    
    public void updateRotationMatrix(String matrix, float rot) {
        matrix = matrix.toUpperCase();
        switch(matrix) {
            case "X":
                CameraModel.rotationMatrixX[1][1] = (float)Math.cos(rot);
                CameraModel.rotationMatrixX[2][2] = (float)Math.cos(rot);
                CameraModel.rotationMatrixX[1][2] = (float)Math.sin(rot);
                CameraModel.rotationMatrixX[2][1] = -(float)Math.sin(rot);
                break;
            case "Y":
                CameraModel.rotationMatrixX[0][0] = (float)Math.cos(rot);
                CameraModel.rotationMatrixX[2][2] = (float)Math.cos(rot);
                CameraModel.rotationMatrixX[2][0] = (float)Math.sin(rot);
                CameraModel.rotationMatrixX[0][2] = -(float)Math.sin(rot);
                break;
            case "Z":
                CameraModel.rotationMatrixZ[0][0] = (float)Math.cos(rot);
                CameraModel.rotationMatrixZ[1][1] = (float)Math.cos(rot);
                CameraModel.rotationMatrixZ[0][1] = (float)Math.sin(rot);
                CameraModel.rotationMatrixZ[1][0] = -(float)Math.sin(rot);
                break;
        }
    }
}
