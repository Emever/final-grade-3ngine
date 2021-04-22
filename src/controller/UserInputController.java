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
    
    private boolean[] inputWASD;    // true if pressed, false otherwise
    private boolean[] inputUDLR;    // same, 1 is pressed, 0 once released
    
    public UserInputController(EngineController controller) {
        this.engineController = controller;
        this.inputWASD = new boolean[4];
        for (boolean i:this.inputWASD)
            i = false;
        this.inputUDLR = new boolean[4];
        for (boolean i:this.inputUDLR)
            i = false;
        
    }

    public boolean[] getInputWASD() {
        return inputWASD;
    }
    public void setInputWASD(boolean[] inputWASD) {
        this.inputWASD = inputWASD;
    }
    public boolean[] getInputUDLR() {
        return inputUDLR;
    }
    public void setInputUDLR(boolean[] input) {
        this.inputUDLR = input;
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
