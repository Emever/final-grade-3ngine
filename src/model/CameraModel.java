package model;

import controller.EngineController;
import controller.EngineLoopThread;
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
    
    private Vertex pos;
    private float posSpeed;
    private Vertex rot; // radians
    private Vertex rotSpeed;  // radians
    
    //public static float fTheta;
    public static float[][] projectionMatrix;
    public static float[][] translationMatrix;
    public static float[][] rotationMatrixX, rotationMatrixZ, rotationMatrixY;
    
    public CameraModel(EngineController controller) {
        this.engine = controller;
        this.pos = new Vertex(0.0f,0.0f,0.0f, "Camera position");
        this.posSpeed = 1.5f;
        
        this.rot = new Vertex(0.0f, 0.0f, 0.0f);
        this.rotSpeed = new Vertex(UtilsMath.DegToRads(5), UtilsMath.DegToRads(45), 0.0f);
    }

    public Vertex getPos() {
        return pos;
    }
    public void setPos(Vertex pos) {
        this.pos = pos;
    }
    public Vertex getRot() {
        return rot;
    }
    public void setRot(Vertex rot) {
        this.rot = rot;
    }
    public Vertex getRotSpeed() {
        return rotSpeed;
    }
    public void setRotSpeed(Vertex rotSpeed) {
        this.rotSpeed = rotSpeed;
    }
    
    
    
    
    public void init() {
        this.createProjectionMatrix();
        this.createTranslationMatrix();
        this.createRotationMatrixX(this.rot.getX());
        this.createRotationMatrixY(this.rot.getY());
        this.createRotationMatrixZ(this.rot.getZ());
    }
    
    public void createProjectionMatrix() {
        // create the projection matrix (it won't change whilst running)
        CameraModel.projectionMatrix = UtilsMath.getProjectionMatrix();
    }
    public void createTranslationMatrix() {
        CameraModel.translationMatrix = UtilsMath.getTranslationMatrix(
                this.pos.getX(),
                this.pos.getY(),
                this.pos.getZ()
            );
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
    
    public void move(float x, float y, float z) {
        this.pos.translate(x, y, z);
    }
    public void turn(int turnX, int turnY, int turnZ) {
        System.out.println("rotSpeed: " + this.rotSpeed.toString());
        if (turnX != 0)
            this.rot.setX(this.rot.getX() + (float)turnX * this.rotSpeed.getX() * (float)EngineLoopThread.TPFmillis/1000);
        if (turnY != 0)
            this.rot.setY(this.rot.getY() + (float)turnY * this.rotSpeed.getY() * (float)EngineLoopThread.TPFmillis/1000);
        if (turnZ != 0)
            this.rot.setZ(this.rot.getZ() + (float)turnZ * this.rotSpeed.getZ() * (float)EngineLoopThread.TPFmillis/1000);
    }
}
