package view;

import controller.EngineController;
import javax.swing.JComponent;
import javax.swing.JFrame;import java.awt.Color;
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
        
        System.out.println("YA ERA HORA OSTIA");
        
        Shape background = new Rectangle2D.Float(0,0, EngineModel.dimX, EngineModel.dimY);
        this.g2.setColor(Color.DARK_GRAY);
        this.g2.fill(background);
        
        // we draw all the triangles from the scene
        // -> every mesh from the scene
        System.out.println("Dibujando meshes...");
        for (Mesh m:this.engineController.getScene().getMeshList())
            for (Triangle t:m.getTris()) // -> every triangle from every mesh
                this.drawTriangle(t);
    }
    
    public void drawTriangle(Triangle t) {
        // draw the triangles of every mesh into the screen
        this.g2.setColor(Color.CYAN);
        this.g2.drawLine(
                (int)t.getProjectionVertex(0).getX(),
                (int)t.getProjectionVertex(0).getY(),
                (int)t.getProjectionVertex(1).getX(),
                (int)t.getProjectionVertex(1).getY()
            );
        this.g2.drawLine(
                (int)t.getProjectionVertex(1).getX(),
                (int)t.getProjectionVertex(1).getY(),
                (int)t.getProjectionVertex(2).getX(),
                (int)t.getProjectionVertex(2).getY()
            );
        this.g2.drawLine(
                (int)t.getProjectionVertex(2).getX(),
                (int)t.getProjectionVertex(2).getY(),
                (int)t.getProjectionVertex(0).getX(),
                (int)t.getProjectionVertex(0).getY()
            );
       
        // square test
        this.g2.drawLine(
                (int)t.getProjectionVertex(0).getX()-10,
                (int)t.getProjectionVertex(0).getY()-10,
                (int)t.getProjectionVertex(0).getX()+10,
                (int)t.getProjectionVertex(0).getY()+10
            );
        this.g2.setColor(Color.BLUE);
        this.g2.drawLine(
                (int)t.getProjectionVertex(1).getX()-10,
                (int)t.getProjectionVertex(1).getY()-10,
                (int)t.getProjectionVertex(1).getX()+10,
                (int)t.getProjectionVertex(1).getY()+10
            );
        this.g2.setColor(Color.GREEN);
        this.g2.drawLine(
                (int)t.getProjectionVertex(2).getX()-10,
                (int)t.getProjectionVertex(2).getY()-10,
                (int)t.getProjectionVertex(2).getX()+10,
                (int)t.getProjectionVertex(2).getY()+10
            );
    }
}
