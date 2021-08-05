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
    
    private UserInputController inputController;
    private FileController fileController;
    
    public double deltaTime;
    private EngineLoopThread mainLoop;
    private boolean loopIsOn;
    
    private SceneObject scene;
    public static CameraModel camera;
    public static Vertex lightDirection;
    
    private boolean boolFacingCameraActivation = true;
    
    private int renderingMode;
        // 1: Full mode
        // 2: Wireframe mode
        // 3: Surface mode
    
    public EngineController(EngineModel engine) {
        this.loopIsOn = false;
        
        this.engineModel = engine;
        this.fileController = new FileController(this);
        this.inputController = new UserInputController(this);

        this.engineView = new EngineView(this);
        this.renderingMode = 1;
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
    public int getRenderingMode() {
        return renderingMode;
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
        EngineController.camera.getPos().setZ(5f);
        EngineController.camera.init();
        
        // 3. MODIFY THE "TEST SCENE"
        // 3.1. Create the mesh in space
        if (this.scene.getMeshList().isEmpty()) {
            Mesh cube = this.createTestCube();
            //cube.setPos(new Vertex(0,0,6)); // places the cube at (0,0,-6)
            //cube.setAddToRot(new Vertex(0f, UtilsMath.DegToRads(30), 0f));
            this.scene.addMesh(cube);
        }
        // 3.2. Create a light
        EngineController.lightDirection = new Vertex(0.0f, -0.5f, -1.0f, "Light direction of the scene");
        EngineController.lightDirection.normalize();
        
        // 4. ENGINE IS READY TO RENDER
        this.engineView.setVisible(true);
    }
    
    public void stopMainLoop() {
        this.loopIsOn = false;
        if (!isNull(this.mainLoop)) this.mainLoop.interrupt();
        this.mainLoop = null;
    }
    public void closeScene() {
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
        // init cube triangle list and position
        Mesh cube = new Mesh("cube", new Vertex(0.0f, 0.0f, 0.0f));
        /**/
        // SOUTH
        cube.addTriangle(new Vertex(-1f,-1f,-1f),new Vertex(-1f,1f,-1f),new Vertex(1f,1f,-1f));
        cube.addTriangle(new Vertex(-1f,-1f,-1f),new Vertex(1f,1f,-1f),new Vertex(1f,-1f,-1f));
        /**/
        // EAST
        cube.addTriangle(new Vertex(1f,-1f,-1f),new Vertex(1f,1f,-1f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(1f,-1f,-1f),new Vertex(1f,1f,1f),new Vertex(1f,-1f,1f));
        /**/
        // WEST
        cube.addTriangle(new Vertex(-1f,-1f,1f),new Vertex(-1f,1f,1f),new Vertex(-1f,1f,-1f));
        cube.addTriangle(new Vertex(-1f,-1f,1f),new Vertex(-1f,1f,-1f),new Vertex(-1f,-1f,-1f));
        /**/
        // NORTH
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(1f,1f,1f),new Vertex(-1f,1f,1f));
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,1f,1f),new Vertex(-1f,-1f,1f));
        /**/
        // TOP
        cube.addTriangle(new Vertex(-1f,1f,-1f),new Vertex(-1f,1f,1f),new Vertex(1f,1f,1f));
        cube.addTriangle(new Vertex(-1f,1f,-1f),new Vertex(1f,1f,1f),new Vertex(1f,1f,-1f));
        /**/
        // BOT
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,-1f,1f),new Vertex(-1f,-1f,-1f));
        cube.addTriangle(new Vertex(1f,-1f,1f),new Vertex(-1f,-1f,-1f),new Vertex(1f,-1f,-1f));
        /**/
        return cube;
    }
    
    public void loopFunction() {
        this.deltaTime = System.currentTimeMillis();
        float elapsedTime = (float)EngineLoopThread.TPFmillis/1000;
        
        // 1. we check player inputs
        this.applyPlayerInput(elapsedTime);
        
        // 2. update the camera matrixes and reset triangles to project list
        this.scene.clearProjectionList();
        EngineController.camera.update();
        
        // 3. generate the matrixes needed (optimized)
            // 3.1. general transformation matrixes
        float[][] cMatrix_Tra = CameraModel.translationMatrix;
        float[][] cMatrix_RotZ = CameraModel.rotationMatrixZ;
        float[][] cMatrix_RotY = CameraModel.rotationMatrixY;
        float[][] cMatrix_RotX = CameraModel.rotationMatrixX;
        float[][] cMatrix_Transform = UtilsMath.MultiplyMatrixMatrix(
                cMatrix_RotX,
                UtilsMath.MultiplyMatrixMatrix(
                        cMatrix_RotY,
                        UtilsMath.MultiplyMatrixMatrix(
                                cMatrix_RotZ,
                                cMatrix_Tra
                            )
                        )
                    );
            // 3.2. camera projection matrix and scaling to view
        float[][] cMatrix_Proj = CameraModel.projectionMatrix;
        float[][] cMatrix_ScaleToView = new float[][]
            {
                {1.0f, 0.0f, 0.0f, ((float)EngineModel.dimX)/2},
                {0.0f, -1.0f, 0.0f, ((float)EngineModel.dimY)/2},
                {0.0f, 0.0f, 1.0f, 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
            };
        
        // ______________________________________________________ VERTEX PROCESS
        for (Mesh m:this.scene.getMeshList()) {
            
            // we apply the mesh rotation increments to its angle
            m.setPos(UtilsMath.AddVertex(m.getPos(), UtilsMath.MulVertex(m.getAddToPos(), (float)EngineLoopThread.TPFmillis/1000)));
            m.setRot(UtilsMath.AddVertex(m.getRot(), UtilsMath.MulVertex(m.getAddToRot(), (float)EngineLoopThread.TPFmillis/1000)));

            // generate the matrixes needed
            float[][] vMatrix_RotZ = UtilsMath.getRotationMatrix_Z(m.getRot().getZ());
            float[][] vMatrix_RotY = UtilsMath.getRotationMatrix_Y(m.getRot().getY());
            float[][] vMatrix_RotX = UtilsMath.getRotationMatrix_X(m.getRot().getX());
            float[][] vMatrix_Tra = UtilsMath.getTranslationMatrix(
                    m.getPos().getX(),
                    m.getPos().getY(),
                    m.getPos().getZ()
                );
            float[][] vMatrix_MeshTransform = UtilsMath.MultiplyMatrixMatrix(
                vMatrix_Tra,
                UtilsMath.MultiplyMatrixMatrix(
                        vMatrix_RotX,
                        UtilsMath.MultiplyMatrixMatrix(
                                vMatrix_RotZ,
                                vMatrix_RotY
                            )
                        )
                    );
            
            
            for (Triangle t:m.getTris()) {
                //t.setVisible(true);
                
                // 1. VERTEX CALCULATIONS - - - - - - - - - - - - - - - - - - - -
                for (int vIndex=0; vIndex<3; vIndex++) {
                    t.setVProcess(UtilsMath.MultiplyMatrixVector(t.getVList(vIndex), null, vMatrix_MeshTransform), vIndex);
                }
                
                // we got the new vertex data (after rotations, etc.)
                t.calculateVNormal();
                t.setVisible(true);
                if (this.renderingMode != 2)
                    if (this.boolFacingCameraActivation) t.checkIfFacingCamera();
                
                // ______________________________________________ CAMERA PROCESS
                if (t.isVisible()) {
                    if (this.renderingMode != 2)
                        t.calculateLightingValue();
                    
                    // 2. CAMERA VIEW CALCULATIONS _____________________________
                    for (int vIndex=0; vIndex<3; vIndex++) {
                        t.setVView(UtilsMath.MultiplyMatrixVector(t.getVProcess(vIndex), null, cMatrix_Transform), vIndex);
                    }
                    
                    Triangle[] tList = UtilsMath.Triangle_ClipToPlane(CameraModel.cameraPlane, CameraModel.cameraPlaneNormal, t);
                    if (t.isVisible())
                        for (Triangle tList1 : tList)
                            if (!isNull(tList1))
                                this.scene.addTriangleToProject(tList1);
                    
                }
            }
        }
        
        for (Triangle tToProject:this.scene.getTrisToProject()) {
            
            for (int vIndex=0; vIndex<3; vIndex++) {
                // CAMERA PROJECTION + RENDERING _______________________________
                // [9] camera projection matrix -> cMatrix_Proj
                tToProject.setVProjection(UtilsMath.MultiplyMatrixVector(tToProject.getVView(vIndex), null, cMatrix_Proj), vIndex);

                // [10] camera perspective
                float xWithPerspective = tToProject.getVProjection(vIndex).getX()/tToProject.getVProjection(vIndex).getZ();
                float yWithPerspective = tToProject.getVProjection(vIndex).getY()/tToProject.getVProjection(vIndex).getZ();
                tToProject.getVProjection(vIndex).setX(xWithPerspective);
                tToProject.getVProjection(vIndex).setY(yWithPerspective);
                
                // [11] - Scaling to view
                tToProject.setVProjection(UtilsMath.MultiplyMatrixVector(tToProject.getVProjection(vIndex), null, cMatrix_ScaleToView), vIndex);
            }

            tToProject.calculateDepthValue();
        }
        if (this.renderingMode != 2)
            this.scene.sortProjectedTrianglesByDepth();

        this.deltaTime = System.currentTimeMillis() - this.deltaTime;

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
                
            case KeyEvent.VK_U:
                //this.scene.getFromMeshList(0).editTranslate(0,-.1f,0);
                this.boolFacingCameraActivation = !this.boolFacingCameraActivation;
                break;
                
            case KeyEvent.VK_H: // "HELP" command
                System.out.println("________________________________________");
                System.out.println("Camera position:\t" + EngineController.camera.getPos().toString());
                System.out.println("Camera rotation:\t" + EngineController.camera.getRot().toString());
                System.out.println("Camera vDir:\t\t" + EngineController.camera.getvDir().toString());
                break;
                
            case KeyEvent.VK_SPACE:
                this.inputController.setInputSPACE(true);
                break;
                
            case KeyEvent.VK_CONTROL:
                this.inputController.setInputCTRL(true);
                break;
                
            case KeyEvent.VK_1:
                //System.out.println("now rendering on full mode");
                if (this.renderingMode != 1)
                    this.renderingMode = 1;
                break;
                
            case KeyEvent.VK_2:
                //System.out.println("now rendering on wireframe mode");
                if (this.renderingMode != 2)
                    this.renderingMode = 2;
                break;
                
            case KeyEvent.VK_3:
                //System.out.println("now rendering on surface mode");
                if (this.renderingMode != 3)
                    this.renderingMode = 3;
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

    
    public void applyPlayerInput(float t) {
        // we check if camera is moving
        if (this.inputController.getInputWASD()[0])   // W is checked
            EngineController.camera.move("W", t, 0.0f, t);
        if (this.inputController.getInputWASD()[1])   // A is checked
            EngineController.camera.move("A", -t, 0.0f, -t);
        if (this.inputController.getInputWASD()[2])   // S is checked
            EngineController.camera.move("S", -t, 0.0f, -t);
        if (this.inputController.getInputWASD()[3])   // D is checked
            EngineController.camera.move("D", t, 0.0f, t);
        if (this.inputController.isInputCTRL())   // S is checked
            EngineController.camera.move("CTRL", 0.0f, -t, 0.0f);
        if (this.inputController.isInputSPACE())   // D is checked
            EngineController.camera.move("SPACE", 0.0f, t, 0.0f);
        
        // camera YAW (constant Y-axis)
        if (this.inputController.getInputUDLR()[2])   // LEFT ARROW is checked
            EngineController.camera.turn(0,-1,0);
        if (this.inputController.getInputUDLR()[3])   // RIGHT ARROW is checked
            EngineController.camera.turn(0,1,0);
        // camera PITCH (constant X-axis)
        if (this.inputController.getInputUDLR()[0] &&   // UP ARROW is checked
            EngineController.camera.getRot().getX() > CameraModel.maxPitch)
            EngineController.camera.turn(-1,0,0);
        if (this.inputController.getInputUDLR()[1] &&   // DOWN ARROW is checked
            EngineController.camera.getRot().getX() < CameraModel.minPitch)
            EngineController.camera.turn(1,0,0);
    }
    
    public void resetEngineAndScene() {
        System.out.print("1. Pausing thread loop...");
        this.stopMainLoop();
        System.out.println(" done!");

        System.out.print("2. Stopping current scene...");
        this.closeScene();
        System.out.println(" done!");

        System.out.print("3. Reading object file...");
        this.scene = this.fileController.readObjectFile();
        if (!isNull(this.scene)) {
            System.out.println(" done!");

            System.out.print("4. Resuming main loop...");
            this.init();
            this.startLoop();
            System.out.println(" done!");
            
        } else {
            
            this.stopMainLoop();
            if (!isNull(this.scene)) this.closeScene();
            System.out.println(" done!");
            this.exitEngine();
        }
    }
}
