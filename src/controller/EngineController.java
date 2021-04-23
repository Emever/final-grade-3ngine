/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.util.Objects.isNull;
import model.CameraModel;
import model.EngineModel;
import model.SceneObject;
import model.geometry.*;
import utils.UtilsMath;
import view.EngineView;

/**
 *
 * @author Joel
 */
public class EngineController implements KeyListener {
    private EngineModel engineModel;
    private EngineView engineView;
    private EngineLoopThread mainLoop;
    private boolean loopIsOn;
    
    private UserInputController inputController;
    private FileController fileController;
    
    private SceneObject scene;
    public static CameraModel camera;
    public static Vertex lightDirection;
    
    public EngineController(EngineModel engine) {
        this.loopIsOn = false;
        
        this.engineModel = engine;
        this.fileController = new FileController(this);
        this.inputController = new UserInputController(this);

        this.engineView = new EngineView(this);
    }
    
    // Class methods ___________________________________________________________
    
    public EngineView getEngineView() {
        return engineView;
    }
    public void setEngineView(EngineView engineView) {
        this.engineView = engineView;
    }
    public SceneObject getScene() {
        return scene;
    }
    public void setScene(SceneObject scene) {
        this.scene = scene;
    }
    public boolean isLoopOn() {
        return loopIsOn;
    }
    
    
    // Engine algorythm ________________________________________________________
    public void init() {
        // 1. CREATE THE SCENE OBJECT
        // read a scene file
        //this.fileController.openSceneFile();
        //this.scene = this.fileController.parseFileToScene();
        // update the engine vars with the new scene
        if (isNull(this.scene)) {
            this.scene = new SceneObject();
            this.scene.setSceneTitle(EngineModel.DEFAULT_SCENE_NAME);
            this.engineView.updateTitle(this.scene.getTitle());
        }
        
        // 2. CREATE THE CAMERA FOR THE SCENE
        EngineController.camera = new CameraModel(this);
        EngineController.camera.setPos(new Vertex(0.0f, 0.0f, 5.0f));
        EngineController.camera.init();
        
        // 3. MODIFY THE "TEST SCENE"
        // 3.1. Create the mesh in space
        if (this.scene.getMeshList().isEmpty()) {
            Mesh cube = this.createTestCube();
            this.scene.addMesh(cube);
        }
        // 3.2. Create a light
        EngineController.lightDirection = new Vertex(0.0f, 0.0f, -1.0f, "Light direction of the scene");
        EngineController.lightDirection.normalize();
        
        // 4. ENGINE IS READY TO RENDER
        this.engineView.setVisible(true);
    }
    
    public void stop() {
        // 1. NULLIFY THE SCENE OBJECT
        this.scene.deleteMeshes();
        this.scene = null;
        
        // 2. NULLIFY CAMERA AND PLUS VARS
        EngineController.camera = null;
        
        // 3. NULLIFY LIGHTING
        EngineController.lightDirection = null;
        
        // 4. HIDE
        this.engineView.setVisible(false);
    }
    public void startLoop() {
        this.mainLoop = new EngineLoopThread(this);
        this.loopIsOn = true;
        this.mainLoop.start();
    }
    public void exitEngine() {
        this.loopIsOn = false;
        System.out.println(" done!");
        System.out.println("Closing...");
        System.exit(0);
    }
    
    private Mesh createTestCube() {
        Mesh cube = new Mesh("cube", new Vertex(0.0f, 0.0f, 0.0f));
        // SOUTH
        cube.addTriangle(new Vertex(-1f,-1f,-1f),new Vertex(-1f,1f,-1f),new Vertex(1f,1f,-1f));
        cube.addTriangle(new Vertex(-1f,-1f,-1f),new Vertex(1f,1f,-1f),new Vertex(1f,-1f,-1f));
        // EAST
        cube.addTriangle(new Vertex(1f,-1f,-1f),new Vertex(1f,1f,-1f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(1f,-1f,-1f),new Vertex(1f,1f,1f),new Vertex(1f,-1f,1f));
        // WEST
        cube.addTriangle(new Vertex(-1f,-1f,1f),new Vertex(-1f,1f,1f),new Vertex(-1f,1f,-1f));
        cube.addTriangle(new Vertex(-1f,-1f,1f),new Vertex(-1f,1f,-1f),new Vertex(-1f,-1f,-1f));
        // NORTH
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(1f,1f,1f),new Vertex(-1f,1f,1f));
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,1f,1f),new Vertex(-1f,-1f,1f));
        // TOP
        cube.addTriangle(new Vertex(-1f,1f,-1f),new Vertex(-1f,1f,1f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(-1f,1f,-1f),new Vertex(1f,1f,1f),new Vertex(1f,1f,-1f));
        // BOT
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,-1f,1f),new Vertex(-1f,-1f,-1f));
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,-1f,-1f),new Vertex(1f,-1f,-1f));
        
        return cube;
    }
    
    public void loopFunction() {
        
        // we update the camera matrixes
        EngineController.camera.update();
        float elapsedTime = (float)EngineLoopThread.TPFmillis/1000;
        // we check if camera is moving
        if (this.inputController.getInputWASD()[0])   // W is checked
            EngineController.camera.move("W", elapsedTime, 0.0f, elapsedTime);
        if (this.inputController.getInputWASD()[1])   // A is checked
            EngineController.camera.move("A", -elapsedTime, 0.0f, -elapsedTime);
        if (this.inputController.getInputWASD()[2])   // S is checked
            EngineController.camera.move("S", -elapsedTime, 0.0f, -elapsedTime);
        if (this.inputController.getInputWASD()[3])   // D is checked
            EngineController.camera.move("D", elapsedTime, 0.0f, elapsedTime);
        if (this.inputController.isInputCTRL())   // S is checked
            EngineController.camera.move("SPACE", 0.0f, -elapsedTime, 0.0f);
        if (this.inputController.isInputSPACE())   // D is checked
            EngineController.camera.move("CTRL", 0.0f, elapsedTime, 0.0f);
        
        // camera YAW (constant Y-axis)
        if (this.inputController.getInputUDLR()[2])   // LEFT ARROW is checked
            EngineController.camera.turn(0,-1,0);
        if (this.inputController.getInputUDLR()[3])   // RIGHT ARROW is checked
            EngineController.camera.turn(0,1,0);
        // camera PITCH (constant X-axis)
        if (this.inputController.getInputUDLR()[0])   // UP ARROW is checked
            EngineController.camera.turn(1,0,0);
        if (this.inputController.getInputUDLR()[1])   // DOWN ARROW is checked
            EngineController.camera.turn(-1,0,0);
        // camera ROLL (constant Z-axis)
        /*
        if (this.inputController.getInputUDLR()[0])   // add key to config.
            EngineController.camera.turn(0,0,1);
        if (this.inputController.getInputUDLR()[1])   // add key to config.
            EngineController.camera.turn(0,0,-1);
        */
        
        
        // VERTEX PROCESS ______________________________________________________
        for (Mesh m:this.scene.getMeshList()) {
            // we apply the mesh rotation increments to its angle
            m.setPos(UtilsMath.AddVertex(m.getPos(), UtilsMath.MulVertex(m.getAddToPos(), (float)EngineLoopThread.TPFmillis/1000)));
            //m.setRot(UtilsMath.AddVertex(m.getRot(), UtilsMath.MulVertex(m.getAddToRot(), (float)EngineLoopThread.TPFmillis/1000)));

            for (Triangle t:m.getTris()) {
                for (int vIndex=0; vIndex<3; vIndex++) {

                    // we are just gonna modify "vProcess" atribute as vertex transforms
                    UtilsMath.CopyVertexValues(t.getVList(vIndex), t.getVProcess(vIndex));
                    //System.out.println("Original: " + t.getVProcess()[vIndex].toString());

                    // now we can just calculate from and for vProcess

                    // VERTEX PROCESS ______________________________________________________
                    // [1] origin-translation-matrix (vertex process) -> vMatrix_TraOrigin
                    // ---> por ahora sudamos <---

                    // [2] rotZ vertex matrix -> vMatrix_RotZ - - - - - - - - - 
                    float[][] vMatrix_RotZ = UtilsMath.getRotationMatrix_Z(m.getRot().getZ());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, vMatrix_RotZ), vIndex);
                    
                    // [3] rotY vertex matrix -> vMatrix_RotY - - - - - - - - - 
                    float[][] vMatrix_RotY = UtilsMath.getRotationMatrix_Y(m.getRot().getY());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, vMatrix_RotY), vIndex);
                    
                    // [4] rotX vertex matrix -> vMatrix_RotX - - - - - - - - - 
                    float[][] vMatrix_RotX = UtilsMath.getRotationMatrix_X(m.getRot().getX());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, vMatrix_RotX), vIndex);
                    
                    // [5] translation vertex matrix -> vMatrix_Tra - - - - - - 
                    float[][] vMatrix_Tra = UtilsMath.getTranslationMatrix(
                            m.getPos().getX(),
                            m.getPos().getY(),
                            m.getPos().getZ()
                        );
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, vMatrix_Tra), vIndex);

                    
                    // CAMERA PROCESS ______________________________________________________
                    
                    // [9] translation camera matrix -> cMatrix_Tra - - - - - - 
                    float[][] cMatrix_Tra = CameraModel.translationMatrix;
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_Tra), vIndex);
                    //System.out.println("Camera translation: " + t.getVProcess()[vIndex].toString());
                    
                    // [6] rotZ camera matrix -> cMatrix_RotZ - - - - - - - - - 
                    float[][] cMatrix_RotZ = UtilsMath.getRotationMatrix_Z(EngineController.camera.getRot().getZ());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_RotZ), vIndex);
                    // [7] rotY camera matrix -> cMatrix_RotY - - - - - - - - - 
                    float[][] cMatrix_RotY = UtilsMath.getRotationMatrix_Y(EngineController.camera.getRot().getY());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_RotY), vIndex);
                    // [8] rotX camera matrix -> cMatrix_RotX - - - - - - - - - 
                    float[][] cMatrix_RotX = UtilsMath.getRotationMatrix_X(EngineController.camera.getRot().getX());
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_RotX), vIndex);
                    
                    // CAMERA PROJECTION + RENDERING _______________________________________
                    // [10] camera projection matrix -> cMatrix_Proj - - - - - -
                    float[][] cMatrix_Proj = CameraModel.projectionMatrix;
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_Proj), vIndex);
                    //System.out.println("Camera projection: " + t.getVProcess()[vIndex].toString());

                    // [11] camera perspective -> cMatrix_Persp - - - - - - - - 
                    /*
                    float[][] cMatrix_Persp = new float[][] {
                        {1.0f/t.getVProcess(vIndex).getZ(), 0.0f, 0.0f, 0.0f},
                        {0.0f, 1.0f/t.getVProcess(vIndex).getZ(), 0.0f, 0.0f},
                        {0.0f, 0.0f, 1.0f, 0.0f},
                        {0.0f, 0.0f, 0.0f, 1.0f}
                    };
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_Persp), vIndex);
                    */
                    // theoretically this is the same...
                    float xWithPerspective = 0.0f, yWithPerspective = 0.0f;
                    
                    if (t.getVProcess(vIndex).getX() != 0.0f)
                        xWithPerspective = t.getVProcess(vIndex).getX()/t.getVProcess(vIndex).getZ();
                    
                    if (t.getVProcess(vIndex).getY() != 0.0f)
                        yWithPerspective = t.getVProcess(vIndex).getY()/t.getVProcess(vIndex).getZ();
                    
                    t.getVProcess(vIndex).setX(xWithPerspective);
                    t.getVProcess(vIndex).setY(yWithPerspective);
                    
                    
                    // [12] scaling to view -> cMatrix_toView - - - - - - - - - 
                    float[][] cMatrix_toView = new float[][]
                    {
                        {1.0f, 0.0f, 0.0f, ((float)EngineModel.dimX)/2},
                        {0.0f, -1.0f, 0.0f, ((float)EngineModel.dimY)/2},
                        {0.0f, 0.0f, 1.0f, 0.0f},
                        {0.0f, 0.0f, 0.0f, 1.0f}
                    };
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_toView), vIndex);
                    
                    //t.getVProcess(vIndex).scaleToView();
                    //System.out.println("Vertex!\n________________________");
                }
                //System.out.println("Triangle!\n__________________________________________");                
            }
            //System.out.println("Mesh!\n____________________________________________________________");                
        }
        
        
        
        // F. REPAINT
        this.engineView.repaint();
        //System.out.println("Repainted!\n_____________________________________________________________________\n");
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.out.print("Stopping main loop...");
                this.exitEngine();
                break;
            
            case KeyEvent.VK_O:     // OPEN OBJECT FILE
                boolean res = this.fileController.openObjectFileChooser();
                if (res) this.resetEngineAndScene();
                break;
                
            case KeyEvent.VK_W:
                this.inputController.getInputWASD()[0] = true;
                break;
            
            case KeyEvent.VK_S:
                this.inputController.getInputWASD()[2] = true;
                break;
                
            case KeyEvent.VK_A:
                this.inputController.getInputWASD()[1] = true;
                break;
                
            case KeyEvent.VK_D:
                //this.scene.getFromMeshList(0).editTranslate(-.1f,0,0);
                this.inputController.getInputWASD()[3] = true;
                //EngineController.camera.move(1.0f*(float)EngineLoopThread.TPFmillis/1000, 0.0f, 0.0f);
                break;
                
            case KeyEvent.VK_LEFT:
                //this.scene.getFromMeshList(0).editTranslate(0,-.1f,0);
                this.inputController.getInputUDLR()[2] = true;
                break;
            
            case KeyEvent.VK_RIGHT:
                //this.scene.getFromMeshList(0).editTranslate(0,-.1f,0);
                this.inputController.getInputUDLR()[3] = true;
                break;
                
            case KeyEvent.VK_UP:
                //this.scene.getFromMeshList(0).editTranslate(0,.1f,0);
                this.inputController.getInputUDLR()[0] = true;
                break;
                
            case KeyEvent.VK_DOWN:
                //this.scene.getFromMeshList(0).editTranslate(0,-.1f,0);
                this.inputController.getInputUDLR()[1] = true;
                break;
                
            case KeyEvent.VK_H: // "HELP" command
                System.out.println("________________________________________");
                System.out.println("Camera position: " + EngineController.camera.getPos().toString());
                break;
                
            case KeyEvent.VK_R:
                // we set a rotation angle diff
                // recalculate the projections of the triangles
                break;
                
            case KeyEvent.VK_SPACE:
                this.inputController.setInputSPACE(true);
                break;
                
            case KeyEvent.VK_CONTROL:
                this.inputController.setInputCTRL(true);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                this.inputController.getInputWASD()[0] = false;
                break;
            
            case KeyEvent.VK_S:
                this.inputController.getInputWASD()[2] = false;
                break;
                
            case KeyEvent.VK_A:
                this.inputController.getInputWASD()[1] = false;
                break;
                
            case KeyEvent.VK_D:
                this.inputController.getInputWASD()[3] = false;
                break;
                
            case KeyEvent.VK_R:
                // we set a rotation angle diff
                // recalculate the projections of the triangles
                break;
                
            case KeyEvent.VK_LEFT:
                this.inputController.getInputUDLR()[2] = false;
                break;
            
            case KeyEvent.VK_RIGHT:
                this.inputController.getInputUDLR()[3] = false;
                break;
                
            case KeyEvent.VK_UP:
                this.inputController.getInputUDLR()[0] = false;
                break;
                
            case KeyEvent.VK_DOWN:
                this.inputController.getInputUDLR()[1] = false;
                break;
                
            case KeyEvent.VK_SPACE:
                this.inputController.setInputSPACE(false);
                break;
                
            case KeyEvent.VK_CONTROL:
                this.inputController.setInputCTRL(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    
    public void resetEngineAndScene() {
        System.out.print("1. Pausing thread loop...");
        this.loopIsOn = false;
        this.mainLoop.interrupt();
        this.mainLoop = null;
        System.out.println(" done!");

        System.out.print("2. Stopping current scene...");
        this.stop();
        System.out.println(" done!");

        System.out.print("3. Reading object file...");
        this.scene = this.fileController.readObjectFile();
        System.out.println(" done!");

        System.out.print("4. Resuming main loop...");
        this.init();
        this.startLoop();
        System.out.println(" done!");
    }
}
