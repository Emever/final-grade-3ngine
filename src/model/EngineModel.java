/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Joel
 */
public class EngineModel {
    public static final int dimX = 640;
    public static final int dimY = 480;
    
    public static final String title = "3ngine";
    
    public static final String DEFAULT_SCENE_NAME = "scene";
    public static final String DEFAULT_DIRECTORY_NAME = "Scenes";
    
    public static final String[] RENDERING_MODES = {
        "Full render",
        "Wireframe",
        "Surface"
    };
    
    public EngineModel() {
    }
    
}
