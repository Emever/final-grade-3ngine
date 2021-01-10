package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Joel
 */
public class UserInputController extends KeyAdapter {
    private EngineController engineController;
    
    public UserInputController(EngineController controller) {
        this.engineController = controller;
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        System.out.println("Key pressed!");
        engineController.keyPressed(e);                    
    }
    @Override
    public void keyReleased(KeyEvent e){       
        engineController.keyReleased(e);                   
    }
}
