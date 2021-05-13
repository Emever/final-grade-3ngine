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
    
    private ArrayList<Triangle> trisToProject;

    public SceneObject() {
        this.sceneTitle = "";
        this.meshList = new ArrayList<>();
        this.trisToProject = new ArrayList<>();
    }
    public SceneObject(String sceneTitle) {
        this.sceneTitle = sceneTitle;
        this.meshList = new ArrayList<>();
        this.trisToProject = new ArrayList<>();
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
    public ArrayList<Triangle> getTrisToProject() {
        return trisToProject;
    }
    public Triangle getTriToProject(int i) {
        return trisToProject.get(i);
    }
    public void setTrisToProject(ArrayList<Triangle> trisToProject) {
        this.trisToProject = trisToProject;
    }
    public void setTriToProject(int i, Triangle t) {
        this.trisToProject.set(i, t);
    }
    
    public void addMesh(Mesh newMesh) {
        this.meshList.add(newMesh);
    }
    public void clearProjectionList() {
        this.trisToProject.clear();
    }
    public void addTriangleToProject(Triangle newTriangle) {
        this.trisToProject.add(newTriangle);
    }
    
    
    public void sortProjectedTrianglesByDepth() {
        int i0 = 0;
        while (i0 < this.trisToProject.size()-1) {
            if (this.trisToProject.get(i0).isVisible()) {
                int maxLocation = i0;
                float max = this.trisToProject.get(maxLocation).getDepthValue();
                for (int iF=i0+1; iF<this.trisToProject.size(); iF++)
                    if (this.trisToProject.get(iF).isVisible())
                        if (max < this.trisToProject.get(iF).getDepthValue()) {
                            maxLocation = iF;
                            max = this.trisToProject.get(iF).getDepthValue();
                        }
                // SWAP! (only needed if i0 is not the max)
                if (i0 != maxLocation) {
                    Triangle auxT = this.trisToProject.get(i0);
                    this.trisToProject.set(i0, this.trisToProject.get(maxLocation));
                    this.trisToProject.set(maxLocation, auxT);
                }
            }
            i0++;
        }
    }
    
    public void deleteMeshes() {
        for (Mesh m:this.meshList) {
            m.delete();
        }
        this.meshList = null;
        this.sceneTitle = "";
    }
}
