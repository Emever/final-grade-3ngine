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
    public static float[][] rotationMatrixX, rotationMatrixZ;
    
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
        this.createRotationMatrixX();
        this.createRotationMatrixZ();
    }
    
    public void createProjectionMatrix() {
        // create the projection matrix (it won't change whilst running)
        float fNear = .1f;
        float fFar = 1000.0f;
        float fQ = fFar/(fFar-fNear);
        float fFOV = UtilsMath.DegToRads(90f);   // direct value in radians
        float fAspectRatio = (float)EngineModel.dimY / (float)EngineModel.dimX;
        float fFOVRad = 1.0f / (float)Math.tan(fFOV*.5f);    // in radians
        
        float[][] matProj = {
            {fAspectRatio * fFOVRad,    0,  0,  0},
            {0, fFOVRad,    0,  0},
            {0, 0,  fQ,     1.0f},
            {0, 0,  -fNear * fQ,  0}
        };
        CameraModel.projectionMatrix = matProj;
    }
    
    public void createRotationMatrixZ() {
        float[][] rotZ = {
            {(float)Math.cos(EngineController.fTheta), (float)Math.sin(EngineController.fTheta), 0, 0},
            {(float)-Math.sin(EngineController.fTheta), (float)Math.cos(EngineController.fTheta), 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };
        CameraModel.rotationMatrixZ = rotZ;
    }
    
    public void createRotationMatrixX() {
        float[][] rotX = {
            {1, 0, 0, 0},
            {0, (float)Math.cos(EngineController.fTheta/2), (float)Math.sin(EngineController.fTheta/2), 0},
            {0, -(float)Math.sin(EngineController.fTheta/2), (float)Math.cos(EngineController.fTheta/2), 0},
            {0, 0, 0, 1}
        };
        CameraModel.rotationMatrixX = rotX;
    }
    
    public void updateRotationMatrixes() {
        // Z Rotation Matrix
        CameraModel.rotationMatrixZ[0][0] = (float)Math.cos(EngineController.fTheta);
        CameraModel.rotationMatrixZ[1][1] = (float)Math.cos(EngineController.fTheta);
        CameraModel.rotationMatrixZ[0][1] = (float)Math.sin(EngineController.fTheta);
        CameraModel.rotationMatrixZ[1][0] = -(float)Math.sin(EngineController.fTheta);
        // X Rotation Matrix
        CameraModel.rotationMatrixX[1][1] = (float)Math.cos(EngineController.fTheta/2);
        CameraModel.rotationMatrixX[2][2] = (float)Math.cos(EngineController.fTheta/2);
        CameraModel.rotationMatrixX[1][2] = (float)Math.sin(EngineController.fTheta/2);
        CameraModel.rotationMatrixX[2][1] = -(float)Math.sin(EngineController.fTheta/2);
    }
}
