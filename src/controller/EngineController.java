/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    
    public static float[][] projectionMatrix;
    public static float fTheta;
    public static float[][] rotationMatrixX, rotationMatrixZ;
    
    public EngineController(EngineModel engine) {
        this.loopIsOn = false;
        
        this.engineModel = engine;
        this.fileController = new FileController();
        this.inputController = new UserInputController(this);

        this.engineView = new EngineView(this);
    }
    
    public void init() {
        // read a scene file
        this.fileController.openSceneFile();
        this.scene = this.fileController.parseFileToScene();
        // update the engine vars with the new scene
        this.engineView.updateTitle(this.scene.getTitle());
        
        // CREATE ALL MATRIX
        this.createProjectionMatrix();
        EngineController.fTheta = 0.0f;
        this.createRotationMatrixX();
        this.createRotationMatrixZ();
        
        
        
        // DEVELOPING MODIFICATIONS of the SCENE
        // 1. Create the mesh in space
        Mesh cube = this.createTestCube();
        // 2. Calculate its vertexes' projection (frustum)
        cube.calculateAllProjections();
        // EXTRA: THE FIRST TRANSFORMATION WILL BE ROTATION:
        
        EngineController.fTheta = 0f;
        /*
        this.createRotationMatrixX();
        this.createRotationMatrixZ();
        cube.editRotateZ();
        cube.editRotateX();
        */
        // EXTRA: WE TRANSLATE THE TRIANGLE
        cube.editTranslate(0,0,3f);
        // now we need to recalculate the projection
        cube.calculateAllProjections();
        
        
        
        this.scene.addMesh(cube);
        // 3. Render the projection into the screen (automatic)
        this.engineView.repaint();
        
        // render the scene
        this.engineView.setVisible(true);
        
        this.engineView.repaint();

    }
    
    public void startLoop() {
        this.mainLoop = new EngineLoopThread(this);
        this.loopIsOn = true;
        this.mainLoop.start();
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
    
    
    public void loopFunction() {
        //amoave las rotaciones __________________________
        EngineController.fTheta += 1f*EngineLoopThread.elapsedTime/1000;
        this.updateRotationMatrixes();
        this.scene.getMeshList().forEach((m) -> {
            m.editRotateZ();
            m.editRotateX();
            m.getTris().forEach((t) -> {
                // CALCULATE PROJECTION FROM ROTATION VERTEXES
                t.calculateTriangleProjection(t.getvRotation());
                t.scaleVertexToView();
            });
            
        });
        // _______________________________________________
        
        // calculate every mesh' projection
        /*
        this.scene.getMeshList().forEach((m) -> {
            m.calculateAllProjections();
        });
        */
        // finally, repaint
        this.engineView.repaint();
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.out.print("Stopping main loop...");
                this.loopIsOn = false;
                System.out.println(" done!");
                System.out.println("Closing...");
                System.exit(0);
                break;
            
            case KeyEvent.VK_H:     // HELP COMMAND
                System.out.println("Coordinates ____________");
                System.out.println("v0: " + this.scene.getFromMeshList(0).getTriangle(0).getVertex(0).toString());
                System.out.println("v2: " + this.scene.getFromMeshList(0).getTriangle(0).getVertex(0).toString());
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
                EngineController.fTheta += .01f;
                // rotate in Z and then in X axis
                this.updateRotationMatrixes();
                this.scene.getFromMeshList(0).editRotateZ();
                this.scene.getFromMeshList(0).editRotateX();
                // recalculate the projections of the triangles
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent ke) {}

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
        EngineController.projectionMatrix = matProj;
    }
    public void createRotationMatrixZ() {
        float[][] rotZ = {
            {(float)Math.cos(EngineController.fTheta), (float)Math.sin(EngineController.fTheta), 0, 0},
            {(float)-Math.sin(EngineController.fTheta), (float)Math.cos(EngineController.fTheta), 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };
        EngineController.rotationMatrixX = rotZ;
    }
    public void createRotationMatrixX() {
        float[][] rotX = {
            {1, 0, 0, 0},
            {0, (float)Math.cos(EngineController.fTheta/2), (float)Math.sin(EngineController.fTheta/2), 0},
            {0, (float)-Math.sin(EngineController.fTheta/2), (float)Math.cos(EngineController.fTheta/2), 0},
            {0, 0, 0, 1}
        };
        EngineController.rotationMatrixZ = rotX;
    }
    public void updateRotationMatrixes() {
        // Z Rotation Matrix
        EngineController.rotationMatrixZ[0][0] = (float)Math.cos(EngineController.fTheta);
        EngineController.rotationMatrixZ[1][1] = EngineController.rotationMatrixZ[0][0];
        EngineController.rotationMatrixZ[0][1] = (float)Math.sin(EngineController.fTheta);
        EngineController.rotationMatrixZ[1][0] = -EngineController.rotationMatrixZ[0][1];
        // X Rotation Matrix
        EngineController.rotationMatrixX[1][1] = (float)Math.cos(EngineController.fTheta);
        EngineController.rotationMatrixX[2][2] = EngineController.rotationMatrixX[1][1];
        EngineController.rotationMatrixX[1][2] = (float)Math.sin(EngineController.fTheta);
        EngineController.rotationMatrixX[2][1] = -EngineController.rotationMatrixX[1][2];
    }
}
