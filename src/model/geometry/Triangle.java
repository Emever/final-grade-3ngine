package model.geometry;

import java.util.ArrayList;

/**
 *
 * @author Joel
 */
public class Triangle {
    private int id;
    private Vertex[] vlist;
    
    public Triangle() {
        this.id = 0;
        this.vlist = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vlist[i] = new Vertex();
    }
    public Triangle(int id, Vertex v1, Vertex v2, Vertex v3) {
        this.id = 0;
        this.vlist = new Vertex[3];
        this.vlist[0] = v1;
        this.vlist[1] = v2;
        this.vlist[2] = v3;
    }
    public Triangle(int id, Vertex[] vs) {
        this.id = 0;
        this.vlist = new Vertex[3];
        for (int i=0; i<3; i++)
            this.vlist[i] = vs[i];
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Vertex[] getVlist() {
        return vlist;
    }
    public void setVlist(Vertex[] vlist) {
        this.vlist = vlist;
    }
    public Vertex getVertex(int i) {
        if (i < 4 && i > 0)
            return this.vlist[i];
        System.out.println("Wrong requested index! (from triangle)");
        return null;
    }
    public boolean setVertex(int i, Vertex v) {
        if (i < 4 && i > 0) {
            this.vlist[i] = v;
            return true;
        }
        return false;
    } 
    
}
