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
    public static float fTheta;
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
        EngineController.fTheta = 0.0f;
        EngineController.camera = new CameraModel(this);
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
        EngineController.fTheta = 0.0f;
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
        Mesh cube = new Mesh();
        // all vertex
        /*
        Vertex v0 = new Vertex(0f,0f,0f);
        Vertex v1 = new Vertex(0f,1f,0f);
        Vertex v2 = new Vertex(1f,1f,0f);
        Vertex v3 = new Vertex(1f,0f,0f);
        Vertex v4 = new Vertex(0f,0f,1f);
        Vertex v5 = new Vertex(0f,1f,1f);
        Vertex v6 = new Vertex(1f,1f,1f);
        Vertex v7 = new Vertex(1f,0f,1f);
        // now we create the faces:
        // SOUTH
        cube.addTriangle(v0,v1,v2);
        cube.addTriangle(v0,v2,v3);
        // EAST
        cube.addTriangle(v3,v2,v6);
        cube.addTriangle(v3,v6,v7);
        // WEST
        cube.addTriangle(v4,v5,v1);
        cube.addTriangle(v4,v1,v0);
        // NORTH
        cube.addTriangle(v7,v6,v5);
        cube.addTriangle(v7,v5,v4);
        // TOP
        cube.addTriangle(v1,v5,v6);
        cube.addTriangle(v1,v6,v2);
        // BOT
        cube.addTriangle(v7,v4,v0);
        cube.addTriangle(v7,v0,v3);
        */
        // SOUTH
        cube.addTriangle(new Vertex(0f,0f,0f),new Vertex(0f,1f,0f),new Vertex(1f,1f,0f));
        cube.addTriangle(new Vertex(0f,0f,0f),new Vertex(1f,1f,0f),new Vertex(1f,0f,0f));
        // EAST
        cube.addTriangle(new Vertex(1f,0f,0f),new Vertex(1f,1f,0f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(1f,0f,0f),new Vertex(1f,1f,1f),new Vertex(1f,0f,1f));
        // WEST
        cube.addTriangle(new Vertex(0f,0f,1f),new Vertex(0f,1f,1f),new Vertex(0f,1f,0f));
        cube.addTriangle(new Vertex(0f,0f,1f),new Vertex(0f,1f,0f),new Vertex(0f,0f,0f));
        // NORTH
        cube.addTriangle(new Vertex(1f,0f,1f),new Vertex(1f,1f,1f),new Vertex(0f,1f,1f));
        cube.addTriangle(new Vertex(1f,0f,1f),new Vertex(0f,1f,1f),new Vertex(0f,0f,1f));
        // TOP
        cube.addTriangle(new Vertex(0f,1f,0f),new Vertex(0f,1f,1f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(0f,1f,0f),new Vertex(1f,1f,1f),new Vertex(1f,1f,0f));
        // BOT
        cube.addTriangle(new Vertex(1f,0f,1f),new Vertex(0f,0f,1f),new Vertex(0f,0f,0f));
        cube.addTriangle(new Vertex(1f,0f,1f),new Vertex(0f,0f,0f),new Vertex(1f,0f,0f));
        
        return cube;
    }
    
    public void loopFunction() {
        
        EngineController.fTheta += 1f*(float)EngineLoopThread.elapsedTime/1000;
        //System.out.println("Elapsed time: " + EngineLoopThread.elapsedTime);
        //System.out.println("fTheta: " + EngineController.fTheta);
        
        //amoave las transformaciones __________________________
        System.out.println(this.scene.getMeshList().size());
        this.scene.getMeshList().forEach((m) -> {
            
            // 0. RESET VPROCESS VALUES (for the next loop)
            m.initTrianglesVProcess();
           
            // 1. ROTATIONS
            EngineController.camera.updateRotationMatrixes();
            m.editRotateZFromVector("vlist");
            //m.editRotateXFromVector("vprocess");
            
            // 2. TRANSLATING
            m.editTranslate(0,0,40f, "vprocess");
            
            // 2.5. UPDATE TRIANGLES' NORMAL VECTORS + LIGHTING VALUES
            m.loadNormals();
            m.loadLightingValues();
            
            // 3. PROJECTION
            m.calculateAllProjections();
            
        });
        
        // F. REPAINT
        this.engineView.repaint();
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
                this.scene.getFromMeshList(0).editTranslate(0,0,-.1f);
                break;
            
            case KeyEvent.VK_S:
                this.scene.getFromMeshList(0).editTranslate(0,0,.1f);
                break;
                
            case KeyEvent.VK_A:
                this.scene.getFromMeshList(0).editTranslate(.1f,0,0);
                break;
                
            case KeyEvent.VK_D:
                this.scene.getFromMeshList(0).editTranslate(-.1f,0,0);
                break;
                
            case KeyEvent.VK_UP:
                this.scene.getFromMeshList(0).editTranslate(0,.1f,0);
                this.engineView.repaint();
                break;
                
            case KeyEvent.VK_DOWN:
                this.scene.getFromMeshList(0).editTranslate(0,-.1f,0);
                break;
                
            case KeyEvent.VK_R:
                // we set a rotation angle diff
                EngineController.fTheta += 1f*(float)EngineLoopThread.elapsedTime/1000;
                // recalculate the projections of the triangles
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_R:
                // we set a rotation angle diff
                // recalculate the projections of the triangles
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
