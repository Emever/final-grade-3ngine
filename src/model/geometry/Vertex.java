package model.geometry;

import static java.util.Objects.isNull;
import model.EngineModel;

/**
 *
 * @author Joel
 */
public class Vertex {
    private int id;         // numerical identifier
    private float x,y,z;    // cartesian coordinates (in scene)
    
    public Vertex() {
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public Vertex(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    public Vertex(float x, float y, float z) {
        this.id = 0;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vertex(int id, float x, float y, float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vertex(int id, float x, float y, float z, float[][] projMatrix) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString() {
        return "("+this.x+", "+this.y+", "+this.z+")";
    }
    public String toStringWithId() {
        return "id: "+this.id+" - ("+this.x+", "+this.y+", "+this.z+")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }
    
    public void copyValues() {
        
    }
    
    public void scaleToView() {
        this.x = this.x + 1f;
        this.x = this.x * 0.5f * (float)EngineModel.dimX; // scale to half of the screen
        this.y = this.y + 1f;
        this.y = this.y * 0.5f * (float)EngineModel.dimY; // scale to half of the screen
    }
    
    public boolean translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return true;    // we leave it this way in case we need to check anything later on
    }
}
