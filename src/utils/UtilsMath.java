/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import static java.util.Objects.isNull;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class UtilsMath {
    public static float DegToRads(float degreeAngle) {
        return degreeAngle/180 * 3.14159f;
    }
    
    public static void MultiplyMatrixVector(Vertex vI, Vertex vO, float[][] m) {
        // we have to duplicate our inputVector, in case vI equals vO, so
        // our vars doesnt update constatly whilst calculating the new vO.
        Vertex newInput = new Vertex(vI.getX(), vI.getY(), vI.getZ());
        vO.setX(newInput.getX()*m[0][0] + newInput.getY()*m[1][0] + newInput.getZ()*m[2][0] + m[3][0]);
        vO.setY(newInput.getX()*m[0][1] + newInput.getY()*m[1][1] + newInput.getZ()*m[2][1] + m[3][1]);
        vO.setZ(newInput.getX()*m[0][2] + newInput.getY()*m[1][2] + newInput.getZ()*m[2][2] + m[3][2]);
        float w = newInput.getX()*m[0][3] + newInput.getY()*m[1][3] + newInput.getZ()*m[2][3] + m[3][3];
        
        if (w != 0.0f) {
            vO.setX(vO.getX() / w);
            vO.setY(vO.getY() / w);
            vO.setZ(vO.getZ() / w);
        }
    }
    
    public static Vertex GetNormalFromTriangle(Triangle t, Vertex normal) {
        if (isNull(normal)) {
            normal = new Vertex();
        }
        // we need 2 vectors with the same origin vertex: (1)
        Vertex v1 = new Vertex();
        v1.setX(t.getVProcess()[1].getX() - t.getVProcess()[0].getX());
        v1.setY(t.getVProcess()[1].getY() - t.getVProcess()[0].getY());
        v1.setZ(t.getVProcess()[1].getZ() - t.getVProcess()[0].getZ());
        // we need 2 vectors with the same origin vertex: (2)
        Vertex v2 = new Vertex();
        v2.setX(t.getVProcess()[2].getX() - t.getVProcess()[0].getX());
        v2.setY(t.getVProcess()[2].getY() - t.getVProcess()[0].getY());
        v2.setZ(t.getVProcess()[2].getZ() - t.getVProcess()[0].getZ());
        // calculate normal vector
        normal = UtilsMath.CrossProduct(v1, v2);
        // we normalize the vector
        normal.normalize();
        
        return normal;
    }
    public static Vertex GetNormalFromTriangle(Triangle t) {
        // we need 2 vectors with the same origin vertex: (1)
        Vertex v1 = new Vertex();
        v1.setX(t.getVProcess()[1].getX() - t.getVProcess()[0].getX());
        v1.setY(t.getVProcess()[1].getY() - t.getVProcess()[0].getY());
        v1.setZ(t.getVProcess()[1].getZ() - t.getVProcess()[0].getZ());
        // we need 2 vectors with the same origin vertex: (2)
        Vertex v2 = new Vertex();
        v2.setX(t.getVProcess()[2].getX() - t.getVProcess()[0].getX());
        v2.setY(t.getVProcess()[2].getY() - t.getVProcess()[0].getY());
        v2.setZ(t.getVProcess()[2].getZ() - t.getVProcess()[0].getZ());
        // calculate normal vector
        Vertex normal = UtilsMath.CrossProduct(v1, v2);
        // we normalize the vector
        normal.normalize();
        
        return normal;
    }
    
    public static Vertex CrossProduct(Vertex v1, Vertex v2, Vertex result) {
        if (isNull(result)) result = new Vertex();
        
        result.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        result.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        result.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());
        
        return result;
    }
    public static Vertex CrossProduct(Vertex v1, Vertex v2) {
        Vertex result = new Vertex();
        
        result.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        result.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        result.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());
        
        return result;
    }
    
    public static float DotProduct(Vertex v1, Vertex v2) {
        return v1.getX() * v2.getX() +
                v1.getY() * v2.getY() +
                v1.getZ() * v2.getZ();
    }
    
    public static float DotProduct(float[] v1, float[] v2, int l) {
        float result = 0;
        for (int x=0; x<l; x++)
            result += v1[x] * v2[x];
        return result;
    }
    
    public static void CopyVertexValues(Vertex vI, Vertex vO) {
        vO.setX(vI.getX());
        vO.setY(vI.getY());
        vO.setZ(vI.getZ());
    }
}
