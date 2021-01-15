package pkg3ngine;

import controller.EngineController;
import model.*;

/**
 * 3D java engine for videogames
 * @author Joel
 */
public class Main {

    public static void main(String[] args) {
        EngineModel engineModel = new EngineModel();
        EngineController engineController = new EngineController(engineModel);
        
        engineController.init();
        engineController.startLoop();
    }
    
}
