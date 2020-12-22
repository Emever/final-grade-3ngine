package controller;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.*;
import model.geometry.*;

/**
 *
 * @author Joel
 */
public class FileController {
    private String path;
    private File scenesDirectory;
    private File sceneFile;
    
    public FileController() {
        this.sceneFile = null;
        this.scenesDirectory = new File(EngineModel.DEFAULT_DIRECTORY_NAME);
        this.path = "";
        
        try {
            this.scenesDirectory.mkdir();
            this.path = this.scenesDirectory.getCanonicalPath();
            
        } catch(IOException e) {
            //e.printStackTrace();
            System.out.println("Error when trying to create scenes directory.");
        }   
    }
    
    public void openSceneFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files only", "txt", "text");

        
        // Create open file dialog . . . . . . . . . . . . . . . . . . . . . . .
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open your scene");
        fc.setCurrentDirectory(this.scenesDirectory);
        fc.setFileFilter(filter);
        
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File aux = fc.getSelectedFile();
            
            if (aux.exists()) {
                this.sceneFile = aux;
                System.out.println("File was opened successfully!");
                
            } else {
                System.out.println("Something bad happened opening the file");
            }
        }
    }
    
    public SceneObject parseFileToScene() {
        SceneObject newScene = new SceneObject();
        
        try {
            BufferedReader fileInput = new BufferedReader(new FileReader(this.sceneFile));
            // we read the entire document
            this.readFile(fileInput, newScene);
            
            fileInput.close();
            System.out.println("File read!");
            
        } catch(IOException e) {
            System.out.println("An I/O error occurred!");
            System.exit(0);
        }
        
        return newScene;
    }
    
    public void readFile(BufferedReader file, SceneObject scene) throws IOException {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        ArrayList<Vertex> vxList = new ArrayList<Vertex>();
        
        // grid width, height and spacing
        String line = file.readLine();
        String[] segments = line.split(" ");
        
        // nVertex and nVectors
        line = file.readLine();
        
        while (line != null) {
            segments = line.split(" ");
            if (segments[0].equals("vertex")) {
                vxList.add(
                        new Vertex(
                                Integer.parseInt(segments[1]),  // id (int)
                                Integer.parseInt(segments[2]),  // x (float)
                                Integer.parseInt(segments[3])   // y (float)
                            )
                    );
            } else if (segments[0].equals("vector")) {
                Vertex auxIVertex = new Vertex();
                Vertex auxFVertex = new Vertex();
                
                // we find the initial vertex through his id
                for (Vertex v:vxList)
                    if (v.getId() == Integer.parseInt(segments[1]))
                        auxIVertex = v;
                // we find the final vertex through his id
                for (Vertex fv:vxList)
                    if (fv.getId() == Integer.parseInt(segments[2]))
                        auxFVertex = fv;

                vecList.add(new Vector(auxIVertex, auxFVertex));
            }
            
            line = file.readLine();
        }
        
    }
    
}
