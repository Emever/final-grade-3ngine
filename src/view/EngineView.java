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
    private Graphics2D g;
    
    private double framingTime; // in ms
    
    public EngineView(EngineController controller) {
        this.engineController = controller;
        
        this.window = new JFrame();
        //this.window.setUndecorated(true);
        this.window.setSize(EngineModel.dimX, EngineModel.dimY);
        this.window.setResizable(false);
        this.window.setTitle(EngineModel.title);
        this.window.setLocationRelativeTo(null);
        //this.window.setAlwaysOnTop(true);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.window.addKeyListener(controller);
        
        this.window.add(this);
        
        this.framingTime = 0;
        
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
        this.framingTime = System.currentTimeMillis();
        
        this.g = (Graphics2D) g;
        g.clearRect(0, 0, EngineModel.dimX, EngineModel.dimY);
        //this.g2 = (Graphics2D)this.offscreenImg.getGraphics(); // new
        
        
        //System.out.println("Paint starts _________");
        //Shape background = new Rectangle2D.Float(0,0, EngineModel.dimX, EngineModel.dimY);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, EngineModel.dimX, EngineModel.dimY);
        
        // we draw all the triangles from the projetion list of the scene
        this.engineController.getScene().getTrisToProject().forEach((t) -> {
            // -> every triangle from the projection list of the scene
            this.drawTriangle(t);
        });
        
        // Show the "controls" panel info
        this.drawControlPanel();
        
        // Show the debugging and framing info
        this.drawFramingInfoPanel();
        
        this.drawRenderingMode();
    }
    
    public void drawFramingInfoPanel() {
        // background rectangle with alpha
        g.setColor(new Color(0f,0f,0f,.2f));
        g.fillRect(4, 6, 260, 95);
        //Show the frames ratio
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Sans-serif", Font.PLAIN, 12));
        g.drawString("Frame count: " + EngineLoopThread.nFramesLoop+" fps", 16, 25);
        //Show the elapsed time
        g.drawString("Elapsed time: " + EngineLoopThread.elapsedTime+" ms", 16, 41);
        g.setColor(new Color(0f,1f,1f));
        g.drawString("Delta time (operation loop): " + this.engineController.deltaTime+" ms", 16, 57);
        double auxTime = (System.currentTimeMillis() - this.framingTime);
        g.drawString("Framing time (drawing loop): " + auxTime +" ms", 16, 73);
        auxTime = auxTime + this.engineController.deltaTime;
        g.setColor(new Color(1f,.8f,.8f));
        g.drawString("Safe-limit time (full loop): " + auxTime +" ms", 16, 89);
    }
    
    public void drawRenderingMode() {
        // background rectangle with alpha
        g.setColor(new Color(0f,0f,0f,.2f));
        g.fillRect(4, EngineModel.dimY-225, 254, 40);
        // rendering mode
        g.setColor(new Color(.5f,.8f,1f));
        g.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        g.drawString("Rendering mode: " +
                EngineModel.RENDERING_MODES[this.engineController.getRenderingMode()-1] +
                " (" + this.engineController.getRenderingMode() + "/3)",
                13, EngineModel.dimY-200);
    }
    
    public void drawControlPanel() {
        // background rectangle with alpha
        g.setColor(new Color(0f,0f,0f,.3f));
        g.fillRect(4, EngineModel.dimY-180, 384, 140);
        // text
        g.setColor(new Color(.5f,.8f,1f));
        g.setFont(new Font("Sans-serif", Font.PLAIN, 22));
        g.drawString("CONTROLS:", 10, EngineModel.dimY-154);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Sans-serif", Font.PLAIN, 14));
        g.drawString("- WASD to move the camera around (at the same height)", 16, EngineModel.dimY-130);
        g.drawString("- Space/Ctrl to move the camera updwards or downwards", 16, EngineModel.dimY-114);
        g.drawString("- Directional arrows to pan and tilt the camera", 16, EngineModel.dimY-98);
        g.drawString("- \'O\' to open file explorer", 16, EngineModel.dimY-82);
        g.drawString("- 1,2 or 3 to select different rendering modes", 16, EngineModel.dimY-66);
        g.setColor(new Color(1f,0.5f,0.5f));
        g.drawString("- \'Esc\' to exit the program", 16, EngineModel.dimY-50);
    }
    
    public void drawTriangle(Triangle t) {
        // draw the triangles of every mesh into the screen
        if (this.engineController.getRenderingMode() != 2) {
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
        }
        
        if (this.engineController.getRenderingMode() != 3) {
            g.setColor(Color.CYAN);
            if (this.engineController.getRenderingMode() == 1)
                g.setColor(Color.BLUE);
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
}
