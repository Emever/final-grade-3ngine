/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JComponent;
import javax.swing.JFrame;import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import model.EngineModel;

/**
 *
 * @author Joel
 */
public class EngineView extends JComponent {
    private JFrame window;
    private Graphics2D g2;
    
    public EngineView() {
        this.window = new JFrame();
        this.window.setSize(EngineModel.dimX, EngineModel.dimY);
        this.window.setTitle(EngineModel.title);
        this.window.setLocationRelativeTo(null);
        //this.window.setAlwaysOnTop(true);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.window.add(this);
        
    }
    
    @Override
    public void setVisible(boolean setTo) {
        this.window.setVisible(setTo);
    }
    
    public void updateTitle(String newTitle) {
        this.window.setTitle(this.window.getTitle() + " - " + newTitle);
    }
    
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        
        System.out.println("YA ERA HORA OSTIA");
        
        
        Shape background = new Rectangle2D.Float(0,0, 1200, 500);
        this.g2.setColor(Color.DARK_GRAY);
        this.g2.fill(background);
        
        g2.setColor(Color.CYAN);
        g2.drawRect(
                40,
                40,
                50,
                50
            );
    }
}
