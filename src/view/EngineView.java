package view;

import controller.EngineController;
import controller.EngineLoopThread;
import javax.swing.JComponent;
import javax.swing.JFrame;import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import model.*;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class EngineView extends JComponent {
    private EngineController engineController;
    
    private JFrame window;
    private Graphics g;
    
    private Image offscreenImg;    // double buffering process
    private Graphics offscreenG;
    
    public EngineView(EngineController controller) {
        this.engineController = controller;
        
        this.window = new JFrame();
        //this.window.setUndecorated(true);
        this.window.setSize(EngineModel.dimX, EngineModel.dimY);
        this.window.setTitle(EngineModel.title);
        this.window.setLocationRelativeTo(null);
        //this.window.setAlwaysOnTop(true);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.window.addKeyListener(controller);
        
        this.window.add(this);
        
        // we create the image that won't be rendered until the 2xBuffer is done
        //this.offscreen = createImage(EngineModel.dimX, EngineModel.dimY); // new
        //this.g2 = this.offscreen.getGraphics(); // new
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
    /*
    public void paint(Graphics g) {
        this.offscreenImg = createImage(
                EngineModel.dimX,
                EngineModel.dimY
            );
        this.offscreenG = this.offscreenImg.getGraphics();
        
        this.paintOffscreen(this.offscreenG); // now "paint"
        
        //flip
        g.drawImage(this.offscreenImg, 0, 0, this);
        //System.out.println("In here!");
    }
    */
    
    @Override
    public void paint(Graphics g) {
        this.g = (Graphics2D) g;
        g.clearRect(0, 0, EngineModel.dimX, EngineModel.dimY);
        //this.g2 = (Graphics2D)this.offscreenImg.getGraphics(); // new
        
        
        //System.out.println("Paint starts _________");
        //Shape background = new Rectangle2D.Float(0,0, EngineModel.dimX, EngineModel.dimY);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, EngineModel.dimX, EngineModel.dimY);
        
        //Show the frames ratio
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Serif", Font.PLAIN, 12));
        g.drawString(EngineLoopThread.nFramesLoop+" fps", 10, 20);
        
        //Show the elapsed time
        g.setColor(Color.CYAN);
        g.setFont(new Font("Serif", Font.PLAIN, 12));
        g.drawString((int)EngineLoopThread.elapsedTime+" ms", 10, 36);
        
        // we draw all the triangles from every mesh of the scene
        //System.out.print("Drawing meshes...");
        for (Mesh m:this.engineController.getScene().getMeshList())
            for (Triangle t:m.getTris()) // -> every triangle from every mesh
                if (t.isVisible())
                    this.drawTriangle(t);
        
        //this.repaint();
        //System.out.println(" done!");
    }
    
    public void drawTriangle(Triangle t) {
        // draw the triangles of every mesh into the screen
        
        g.setColor(new Color(
            t.getLightingValue(),
            t.getLightingValue(),
            t.getLightingValue())
        );
        g.fillPolygon(
            new int[] {
                (int)t.getVProjection(0).getX(),
                (int)t.getVProjection(1).getX(),
                (int)t.getVProjection(2).getX()
            },
            new int[] {
                (int)t.getVProjection(0).getY(),
                (int)t.getVProjection(1).getY(),
                (int)t.getVProjection(2).getY()
            },
            3   // n vertexes
        );
        
        g.setColor(Color.CYAN);
        g.drawPolygon(
            new int[] {
                (int)t.getVProjection(0).getX(),
                (int)t.getVProjection(1).getX(),
                (int)t.getVProjection(2).getX()
            },
            new int[] {
                (int)t.getVProjection(0).getY(),
                (int)t.getVProjection(1).getY(),
                (int)t.getVProjection(2).getY()
            },
            3   // n vertexes
        );
        
    }
}
