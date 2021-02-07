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
    
    //public static float fTheta;
    public static float[][] projectionMatrix;
    public static float[][] rotationMatrixX, rotationMatrixZ, rotationMatrixY;
    
    public CameraModel(EngineController controller) {
        this.engine = controller;
        this.pos = new Vertex(0,0,0, "Camera position");
    }

    public Vertex getPos() {
        return pos;
    }
    public void setPos(Vertex pos) {
        this.pos = pos;
    }
    
    
    public void init() {
        this.createProjectionMatrix();
        this.createRotationMatrixX(EngineController.fTheta);
        this.createRotationMatrixY(EngineController.fTheta);
        this.createRotationMatrixZ(EngineController.fTheta);
    }
    
    public void createProjectionMatrix() {
        // create the projection matrix (it won't change whilst running)
        float fNear = .1f;
        float fFar = 1000.0f;
        float fQ = fFar/(fFar-fNear);
        float fFOV = UtilsMath.DegToRads(90f);   // direct value in radians
        float fAspectRatio = (float)EngineModel.dimY / (float)EngineModel.dimX;
        float fFOVRad = 1.0f / (float)Math.tan(fFOV*.5f);    // in radians
        
        CameraModel.projectionMatrix = new float[][]
        {
            {fAspectRatio * fFOVRad,    0,  0,  0},
            {0, fFOVRad,    0,  0},
            {0, 0,  fQ,     1.0f},
            {0, 0,  -fNear * fQ,  0}
        };
    }
    
    public void createRotationMatrixX(float theta) {
        CameraModel.rotationMatrixX = new float[][]
        {
            {1, 0, 0, 0},
            {0, (float)Math.cos(theta), (float)Math.sin(theta), 0},
            {0, -(float)Math.sin(theta), (float)Math.cos(theta), 0},
            {0, 0, 0, 1}
        };
    }
    public void createRotationMatrixY(float theta) {
        CameraModel.rotationMatrixY = new float[][]
        {
            {(float)Math.cos(theta), 0, (float)Math.sin(theta), 0},
            {0, 1, 0, 0},
            {(float)-Math.sin(theta), 0, (float)Math.cos(theta), 0},
            {0, 0, 0, 1}
        };
    }
    public void createRotationMatrixZ(float theta) {
        CameraModel.rotationMatrixZ = new float[][]
        {
            {(float)Math.cos(theta), (float)Math.sin(theta), 0, 0},
            {(float)-Math.sin(theta), (float)Math.cos(theta), 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };
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
