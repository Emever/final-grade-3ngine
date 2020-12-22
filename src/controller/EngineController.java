/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.EngineModel;
import model.SceneObject;
import view.EngineView;

/**
 *
 * @author Joel
 */
public class EngineController {
    private EngineModel engineModel;
    private EngineView engineView;
    
    private FileController fileController;
    
    private SceneObject scene;

    
    public EngineController(EngineModel engine) {
        this.engineModel = engine;
        this.engineView = new EngineView();
        
        this.fileController = new FileController();
    }
    
    public void init() {
        this.fileController.openSceneFile();
        this.scene = this.fileController.parseFileToScene();
        
        this.engineView.updateTitle(this.scene.getTitle());
        this.engineView.setVisible(true);
    }
    
}
