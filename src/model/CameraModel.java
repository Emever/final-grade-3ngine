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
    private float moveSpeed;
    private Vertex rot; // radians
    private Vertex rotSpeed;  // radians
    
    public static final float maxPitch = -(float)Math.PI/2 + 0.1f;
    public static final float minPitch = (float)Math.PI/2 - 0.1f;
    private Vertex vDir;
    private Vertex vRight, vUp;
    
    public static Vertex cameraPlane;
    public static Vertex cameraPlaneNormal;
    
    //public static float fTheta;
    public static float[][] projectionMatrix;
    public static float[][] translationMatrix;
    public static float[][] rotationMatrixX, rotationMatrixZ, rotationMatrixY;
    
    public CameraModel(EngineController controller) {
        this.engine = controller;
        this.pos = new Vertex(0.0f,0.0f,0.0f, "Camera position");
        this.moveSpeed = 2f;
        
        this.rot = new Vertex(0.0f, 0.0f, 0.0f);
        this.rotSpeed = new Vertex(UtilsMath.DegToRads(60), UtilsMath.DegToRads(90), 0.0f);
        
        CameraModel.cameraPlane = new Vertex(0f,0f,-0.1f);
        CameraModel.cameraPlaneNormal = new Vertex(0f,0f,1f);
        
        this.vDir = new Vertex(0.0f, 0.0f, 0.0f);
        this.vRight = new Vertex(0.0f, 0.0f, 0.0f);
        this.vUp = new Vertex(0.0f, 1.0f, 0.0f);
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
    public Vertex getvDir() {
        return vDir;
    }
    public void setvDir(Vertex vDir) {
        this.vDir = vDir;
    }
    public Vertex getvRight() {
        return vRight;
    }
    public void setvRight(Vertex vRight) {
        this.vRight = vRight;
    }
    public Vertex getVUp() {
        return vUp;
    }
    public void setVUp(Vertex vUp) {
        this.vUp = vUp;
    }
    
    
    
    
    
    public void init() {
        this.vDir = UtilsMath.SubVertex(new Vertex(0f,0f,0f), this.pos);
        this.vDir.normalize();
        
        this.vRight = UtilsMath.CrossProduct(this.vDir, this.vUp, null);
        this.vRight.normalize();
        
        this.createProjectionMatrix();
        this.update();
    }
    
    public void update() {
        // we update the matrixes that have some new value (pos, rot...)
        this.createTranslationMatrix();
        this.createRotationMatrixX(this.rot.getX());
        this.createRotationMatrixY(this.rot.getY());
        this.createRotationMatrixZ(this.rot.getZ());
        
        // update vDir vector (so we can move properly towards cam direction)
        this.vDir.setX((float)Math.cos(-1.0f*(float)Math.PI/2 + this.rot.getY()));
        this.vDir.setZ(((float)Math.sin(-1.0f*(float)Math.PI/2 + this.rot.getY())));
        this.vDir.normalize();
        
        this.vRight = UtilsMath.CrossProduct(this.vDir, this.vUp, null);
        this.vRight.normalize();
        
        //System.out.println("camera vRight:\t" + this.vRight.toString());
        //System.out.println("Camera vDirection:\t" + this.vDir.toString());
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
    
    public void move(String c, float x, float y, float z) {
        Vertex moveTo = new Vertex();
        if (c.toUpperCase().equals("W") ||
            c.toUpperCase().equals("S")) {
            moveTo = this.vDir;
        } else if (c.toUpperCase().equals("A") ||
            c.toUpperCase().equals("D")) {
            moveTo = this.vRight;
        }
        this.pos.translate(
                x * this.moveSpeed * moveTo.getX(),
                y * this.moveSpeed * this.vUp.getY(),
                z * this.moveSpeed * moveTo.getZ()
            );
    }
    
    public void turn(int turnX, int turnY, int turnZ) {
        if (turnX != 0)
            this.rot.setX(this.rot.getX() + (float)turnX * this.rotSpeed.getX() * (float)EngineLoopThread.TPFmillis/1000);
        if (turnY != 0)
            this.rot.setY(this.rot.getY() + (float)turnY * this.rotSpeed.getY() * (float)EngineLoopThread.TPFmillis/1000);
        if (turnZ != 0)
            this.rot.setZ(this.rot.getZ() + (float)turnZ * this.rotSpeed.getZ() * (float)EngineLoopThread.TPFmillis/1000);
        
        // we cut off values higher than 2PI
        if (this.getRot().getX() >= 2*(float)Math.PI)   // +too much X rotation
            this.getRot().setX(this.getRot().getX() - 2*(float)Math.PI);
        else if (this.getRot().getX() <= -2*(float)Math.PI)  // -too less X rot
            this.getRot().setX(this.getRot().getX() + 2*(float)Math.PI);
        if (this.getRot().getY() >= 2*(float)Math.PI)   // +too much Y rotation
            this.getRot().setY(this.getRot().getY() - 2*(float)Math.PI);
        else if (this.getRot().getY() <= -2*(float)Math.PI)  // -too less Y rot
            this.getRot().setY(this.getRot().getY() + 2*(float)Math.PI);
        if (this.getRot().getZ() >= 2*(float)Math.PI)   // +too much Z rotation
            this.getRot().setZ(this.getRot().getZ() - 2*(float)Math.PI);
        else if (this.getRot().getZ() <= -2*(float)Math.PI)  // -too less Z rot
            this.getRot().setZ(this.getRot().getZ() + 2*(float)Math.PI);
        
        //System.out.println("\n___________\nrot: " + this.rot.toString());
    }
}
