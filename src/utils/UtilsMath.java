/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controller.EngineController;
import static java.util.Objects.isNull;
import model.EngineModel;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class UtilsMath {
    
    public static float[][] IdentityMatrix = new float[][]
    {
        {1f,0,  0,   0},
        {0, 1f, 0,  0},
        {0, 0,  1f,  0},
        {0, 0,  0,  1f}
    };
    
    /* COMMON ANGULAR OPERATIONS ____________________________________________ */
    
    public static float DegToRads(float degreeAngle) {
        return degreeAngle/180 * 3.14159f;
    }
    
    
    /* COMMON OPS WITH VERTEX/VECTORS _______________________________________ */
    
    public static void CopyVertexValues(Vertex vI, Vertex vO) {
        vO.setX(vI.getX());
        vO.setY(vI.getY());
        vO.setZ(vI.getZ());
        vO.setW(vI.getW());
    }
    public static Vertex AddVertex(Vertex v1, Vertex v2) {
        return new Vertex(
                v1.getX() + v2.getX(),
                v1.getY() + v2.getY(),
                v1.getZ() + v2.getZ()
        );
    }
    public static void AddExistentVertex(Vertex vAdd, Vertex vOut) {
        vOut.translate(vAdd.getX(), vAdd.getY(), vAdd.getZ());
    }
    public static Vertex SubVertex(Vertex v1, Vertex v2) {
        return new Vertex(
                v1.getX() - v2.getX(),
                v1.getY() - v2.getY(),
                v1.getZ() - v2.getZ()
        );
    }
    public static void SubExistentVertex(Vertex vAdd, Vertex vOut) {
        vOut.translate(-1f*vAdd.getX(), -1f*vAdd.getY(), -1f*vAdd.getZ());
    }
    public static Vertex MulVertex(Vertex v1, float t) {
        return new Vertex(
                v1.getX() * t,
                v1.getY() * t,
                v1.getZ() *t
        );
    }
    public static void MulExistentVertex(Vertex v1, float t) {
        v1.scale(t);
    }
    public static Vertex DivVertex(Vertex v1, float t) {
        return new Vertex(
                v1.getX() / t,
                v1.getY() / t,
                v1.getZ() / t
        );
    }
    public static void DivExistentVertex(Vertex v1, float t) {
        v1.scale(1.0f/t);
    }
    
    
    /* MORE OPS WITH VERTEX/VECTORS _________________________________________ */
    
    public static Vertex CrossProduct(Vertex v1, Vertex v2, Vertex result) {
        if (isNull(result)) result = new Vertex();
        
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
    
    
    /* COMMON MATRIX OPERATIONS _____________________________________________ */
    
    public static float[][] getIdentityMatrix() {
        return new float[][] {
            {1.0f,0,0,0},
            {0,1.0f,0,0},
            {0,0,1.0f,0},
            {0,0,0,1.0f}
        };
    }
    
    public static float[][] getProjectionMatrix() {
        
        float focalLength = .1f;
        float vX = focalLength * (float)EngineModel.dimX / (2* ((float)EngineModel.dimX/10000));
        float vY = focalLength * (float)EngineModel.dimY / (2* ((float)EngineModel.dimY/10000));
        
        return new float[][] {
            {vX,    0.0f,   0.0f,   0.0f},
            {0.0f,  vY,     0.0f,   0.0f},
            {0.0f,  0.0f,   -1.0f,  0.0f},
            {0.0f,  0.0f,   0.0f,   1.0f}
        };
        /*
        float fFOVRad = UtilsMath.DegToRads(90);
        float FOVfactor = 1.0f / (float)Math.tan(fFOVRad*.5f);
        float fFar = 1000.0f;
        float fNear = 0.1f;
        float aspectRatio = EngineModel.dimY/EngineModel.dimX;
        float fQ = fFar/(fFar-fNear);
        return new float[][]
        {
            {aspectRatio * FOVfactor,    0,  0,  0},
            {0, FOVfactor,    0,  0},
            {0, 0,  fQ,     1.0f},
            {0, 0,  -fNear * fQ,  0}
        };
        /**/
    }
    
    public static float[][] getTranslationMatrix(float x, float y, float z) {
        return new float[][] {
            {1.0f,  0.0f,   0.0f,   -1.0f*x},
            {0.0f,  1.0f,   0.0f,   -1.0f*y},
            {0.0f,  0.0f,   1.0f,   -1.0f*z},
            {0.0f,  0.0f,   0.0f,   1.0f}
        };
    }
    
    public static float[][] getRotationMatrix_X(float angle) {
        return new float[][] {
            {1.0f,0,0,0},
            {0,(float)Math.cos(angle),-(float)Math.sin(angle),0},
            {0,(float)Math.sin(angle),(float)Math.cos(angle),0},
            {0,0,0,1.0f}
        };
    }
    public static float[][] getRotationMatrix_Y(float angle) {
        return new float[][] {
            {(float)Math.cos(angle),0,(float)Math.sin(angle),0},
            {0,1.0f,0,0},
            {-(float)Math.sin(angle),0,(float)Math.cos(angle),0},
            {0,0,0,1.0f}
        };
    }
    public static float[][] getRotationMatrix_Z(float angle) {
        return new float[][] {
            {(float)Math.cos(angle),-(float)Math.sin(angle),0,0},
            {(float)Math.sin(angle),(float)Math.cos(angle),0,0},
            {0,0,1.0f,0},
            {0,0,0,1.0f}
        };
    }
    
    public static float[][] getMatrix_PointAt(Vertex vPos, Vertex vTarget, Vertex vUp) {
        // create forward vector
        Vertex vForward = UtilsMath.SubVertex(vTarget, vPos);
        vForward.normalize();
        
        // create new up vector
        Vertex a = UtilsMath.MulVertex(vForward, UtilsMath.DotProduct(vUp, vForward));
        Vertex newVUp = UtilsMath.SubVertex(vUp, a);
        newVUp.normalize();
        
        // we can now calculate the new right direction (vector)
        Vertex newVRight = UtilsMath.CrossProduct(newVUp, vForward, null);
        
        return new float[][]
        {
            {newVRight.getX(),  newVRight.getY(),   newVRight.getZ(),   0.0f},
            {newVUp.getX(),     newVUp.getY(),      newVUp.getZ(),      0.0f},
            {vForward.getX(),   vForward.getY(),    vForward.getZ(),    0.0f},
            {vPos.getX(),       vPos.getY(),        vPos.getZ(),        1.0f}
        };
        
    }
    
    public static float[][] MatrixQuickInverse(float[][] mat) {
        float[][] newMat = new float[][]
        {
            {mat[0][0], mat[1][0], mat[2][0], 0.0f},
            {mat[0][1], mat[1][1], mat[2][1], 0.0f},
            {mat[0][2], mat[1][2], mat[2][2], 0.0f},
            {0.0f, 0.0f, 0.0f, 1.0f}
        };
        newMat[3][0] = -(mat[3][0]*newMat[0][0] + mat[3][1]*newMat[1][0] + mat[3][2]*newMat[2][0]);
        newMat[3][1] = -(mat[3][0]*newMat[0][1] + mat[3][1]*newMat[1][1] + mat[3][2]*newMat[2][1]);
        newMat[3][2] = -(mat[3][0]*newMat[0][2] + mat[3][1]*newMat[1][2] + mat[3][2]*newMat[2][2]);
        
        return newMat;
    }
    
    
    public static float[][] MultiplyMatrixMatrix(float[][] m1, float[][] m2) {
        float[][] mO = UtilsMath.getIdentityMatrix();
        
        for (int r=0; r<4; r++)
            for (int c=0; c<4; c++)
                mO[r][c] = m1[r][0]*m2[0][c] + m1[r][1]*m2[1][c] + m1[r][2]*m2[2][c] + m1[r][3]*m2[3][c];
        
        return mO;
    }
    
    public static Vertex MultiplyMatrixVector(Vertex vI, Vertex vO, float[][] m) {
        if (isNull(vO)) vO = new Vertex();
        
        // we have to duplicate our inputVector, in case vI equals vO, so
        // our vars doesnt update constatly whilst calculating the new vO.
        Vertex newInput = new Vertex(vI);
        vO.setX(newInput.getX()*m[0][0] + newInput.getY()*m[0][1] + newInput.getZ()*m[0][2] + newInput.getW()*m[0][3]);
        vO.setY(newInput.getX()*m[1][0] + newInput.getY()*m[1][1] + newInput.getZ()*m[1][2] + newInput.getW()*m[1][3]);
        vO.setZ(newInput.getX()*m[2][0] + newInput.getY()*m[2][1] + newInput.getZ()*m[2][2] + newInput.getW()*m[2][3]);
        vO.setW(newInput.getX()*m[3][0] + newInput.getY()*m[3][1] + newInput.getZ()*m[3][2] + newInput.getW()*m[3][3]);
        
        return vO;
    }
    
}