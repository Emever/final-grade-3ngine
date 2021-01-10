package model.geometry;

/**
 *
 * @author Joel
 */
public class Vector {
    private int id;
    private Vertex iVertex;
    private Vertex fVertex;
    private float dx, dy;   // x and y diff between i and f vertex
    
    public Vector() {
        this.id = 0;
        this.iVertex = new Vertex();
        this.fVertex = new Vertex();
        this.dx = 0;
        this.dy = 0;
    }
    public Vector(Vertex iV, Vertex fV) {
        this.id = 0;
        this.iVertex = iV;
        this.fVertex = fV;
        this.dx = this.getDiff('x', iV, fV);
        this.dy = this.getDiff('y', iV, fV);
    }
    public Vector(int id, Vertex iV, Vertex fV) {
        this.id = id;
        this.iVertex = iV;
        this.fVertex = fV;
        this.dx = this.getDiff('x', iV, fV);
        this.dy = this.getDiff('y', iV, fV);
    }
    public Vector(Vertex iV, Vertex fV, float dX, float dY) {
        this.iVertex = iV;
        this.fVertex = fV;
        this.dx = dX;
        this.dy = dY;
    }
    public Vector(int id, Vertex iV, Vertex fV, float dX, float dY) {
        this.id = id;
        this.iVertex = iV;
        this.fVertex = fV;
        this.dx = dX;
        this.dy = dY;
    }
    
    public Vertex getiVertex() {
        return iVertex;
    }
    public void setiVertex(Vertex iVertex) {
        this.iVertex = iVertex;
    }
    public Vertex getfVertex() {
        return fVertex;
    }
    public void setfVertex(Vertex fVertex) {
        this.fVertex = fVertex;
    }
    public float getDx() {
        return dx;
    }
    public void setDx(float dx) {
        this.dx = dx;
    }
    public float getDy() {
        return dy;
    }
    public void setDy(float dy) {
        this.dy = dy;
    }
    
    @Override
    public String toString() {
        return this.iVertex.toString() + " to " + this.fVertex.toString();
    }
    public String toStringWithId() {
        return this.iVertex.toStringWithId() + " to " + this.fVertex.toStringWithId();
    }
    
    public float getDiff(char axis, Vertex iC, Vertex fC) {
        switch(axis) {
            case 'x':
                return fC.getX() - iC.getX();
            case 'y':
                return fC.getY() - iC.getY();
            case 'z':
                return fC.getZ() - iC.getZ();
            default:
                return 0;
        }
    }
}
