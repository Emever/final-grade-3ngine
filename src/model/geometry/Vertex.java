package model.geometry;

/**
 *
 * @author Joel
 */
public class Vertex {
    private int id;         // numerical identifier
    private float x,y,z;    // cartesian coordinates
    
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
    public Vertex(int id, float x, float y, float z) {
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
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
    
}
