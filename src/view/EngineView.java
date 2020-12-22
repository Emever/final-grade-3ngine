/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JComponent;
import javax.swing.JFrame;
import model.EngineModel;

/**
 *
 * @author Joel
 */
public class EngineView extends JComponent {
    private JFrame window;
    
    public EngineView() {
        this.window = new JFrame();
        this.window.setSize(EngineModel.dimX, EngineModel.dimY);
        this.window.setTitle(EngineModel.title);
        this.window.setLocationRelativeTo(null);
        //this.window.setAlwaysOnTop(true);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void setVisible(boolean setTo) {
        this.window.setVisible(setTo);
    }
    
    public void updateTitle(String newTitle) {
        this.window.setTitle(this.window.getTitle() + " - " + newTitle);
    }
}
