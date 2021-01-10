package model;

import java.util.ArrayList;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class SceneObject {
    private ArrayList<Mesh> meshList;
    
    public String sceneTitle;

    public SceneObject() {
        this.sceneTitle = "";
        this.meshList = new ArrayList<Mesh>();
    }
    public SceneObject(String sceneTitle) {
        this.sceneTitle = sceneTitle;
        this.meshList = new ArrayList<Mesh>();
    }

    public String getTitle() {
        return sceneTitle;
    }
    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }
    public ArrayList<Mesh> getMeshList() {
        return meshList;
    }
    public void setMeshList(ArrayList<Mesh> meshList) {
        this.meshList = meshList;
    }
    public Mesh getFromMeshList(int index) {
        return this.meshList.get(index);
    }
    
    public void addMesh(Mesh newMesh) {
        this.meshList.add(newMesh);
    }
}
