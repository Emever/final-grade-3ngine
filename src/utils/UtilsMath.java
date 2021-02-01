/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

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
    
    public static void CopyVertexValues(Vertex vI, Vertex vO) {
        vO.setX(vI.getX());
        vO.setY(vI.getY());
        vO.setZ(vI.getZ());
    }
}
