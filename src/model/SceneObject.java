/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class SceneObject {
    private ArrayList<Triangle> triangles;
    
    public String sceneTitle;

    public SceneObject() {
        this.sceneTitle = "";
    }

    public SceneObject(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public String getTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }
    
    
    
    
}
