package view;

import controller.EngineController;
import controller.EngineLoopThread;
import javax.swing.JComponent;
import javax.swing.JFrame;import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import model.*;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class EngineView extends JComponent {
    private EngineController engineController;
    
    private JFrame window;
    private Graphics2D g2;
    
    public EngineView(EngineController controller) {
        this.engineController = controller;
        
        this.window = new JFrame();
        this.window.setUndecorated(true);
        this.window.setSize(EngineModel.dimX, EngineModel.dimY);
        this.window.setTitle(EngineModel.title);
        this.window.setLocationRelativeTo(null);
        //this.window.setAlwaysOnTop(true);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.window.addKeyListener(controller);
        
        this.window.add(this);
        
    }
    
    @Override
    public void setVisible(boolean setTo) {
        this.window.setVisible(setTo);
    }

    public JFrame getWindow() {
        return window;
    }
    
    public void updateTitle(String newTitle) {
        this.window.setTitle(this.window.getTitle() + " - " + newTitle);
    }
    
    @Override
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        
        //System.out.println("Paint starts _________");
        Shape background = new Rectangle2D.Float(0,0, EngineModel.dimX, EngineModel.dimY);
        this.g2.setColor(Color.DARK_GRAY);
        this.g2.fill(background);
        
        //Show the frames ratio
        this.g2.setColor(Color.YELLOW);
        this.g2.setFont(new Font("Serif", Font.PLAIN, 12));
        this.g2.drawString(EngineLoopThread.nFramesLoop+" fps", 10, 20);
        
        // we draw all the triangles from every mesh of the scene
        //System.out.print("Drawing meshes...");
        for (Mesh m:this.engineController.getScene().getMeshList())
            for (Triangle t:m.getTris()) // -> every triangle from every mesh
                if (t.isVisible()) this.drawTriangle(t);
        
        //System.out.println(" done!");
    }
    
    public void drawTriangle(Triangle t) {
        // draw the triangles of every mesh into the screen
        this.g2.setColor(new Color(
            t.getLightingValue(),
            t.getLightingValue(),
            t.getLightingValue())
        );
        this.g2.fillPolygon(
            new int[] {
                (int)t.getProjectionVertex(0).getX(),
                (int)t.getProjectionVertex(1).getX(),
                (int)t.getProjectionVertex(2).getX()
            },
            new int[] {
                (int)t.getProjectionVertex(0).getY(),
                (int)t.getProjectionVertex(1).getY(),
                (int)t.getProjectionVertex(2).getY()
            },
            3   // n vertexes
        );
        /*
        this.g2.setColor(Color.CYAN);
        this.g2.drawPolygon(
            new int[] {
                (int)t.getProjectionVertex(0).getX(),
                (int)t.getProjectionVertex(1).getX(),
                (int)t.getProjectionVertex(2).getX()
            },
            new int[] {
                (int)t.getProjectionVertex(0).getY(),
                (int)t.getProjectionVertex(1).getY(),
                (int)t.getProjectionVertex(2).getY()
            },
            3   // n vertexes
        );*/
    }
}
