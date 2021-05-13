package controller;

import java.io.*;
import java.util.ArrayList;
import static java.util.Objects.isNull;
import java.util.Scanner;
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
    private EngineController engine;
    
    public FileController(EngineController controller) {
        this.engine = controller;
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
        } else {
            this.engine.exitEngine();
        }
    }
    
    public SceneObject parseFileToScene() {
        SceneObject newScene = null;
        
        try {
            BufferedReader fileInput = new BufferedReader(new FileReader(this.sceneFile));
            // we read the entire document
            newScene = this.readFile(fileInput);
            
            fileInput.close();
            System.out.println("File read!");
            
        } catch(IOException e) {
            System.out.println("An I/O error occurred!");
            System.exit(0);
        }
        
        return newScene;
    }
    
    public SceneObject readFile(BufferedReader file) throws IOException {
        SceneObject scene = new SceneObject();
        scene.setSceneTitle(EngineModel.DEFAULT_SCENE_NAME);
        
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
        
        // todo: convert from vectors to triangles.
        // todo: add those triangles to the sceneObject.
        // todo (opt): read the scene title (file title) - somehow.
                
        return scene;
    }

    public boolean openObjectFileChooser() {
        boolean openFile = false;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open your scene file (.obj)");
        fc.setCurrentDirectory(this.scenesDirectory);
        fc.setFileFilter(new FileNameExtensionFilter("Obj files only", "obj"));
        
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File newFile = fc.getSelectedFile();
            
            if (newFile.exists()) {
                this.sceneFile = newFile;
                System.out.println("File was opened successfully!");
                openFile = true;
                
            } else {
                System.out.println("Something bad happened opening the file");
            }
        }
        
        return openFile;
    }
    
    public SceneObject readObjectFile() {
        SceneObject scene = null;
        
        try {
            
            String sceneTitle = EngineModel.DEFAULT_SCENE_NAME;
            ArrayList<Vertex> vxList = new ArrayList<Vertex>();
            ArrayList<Triangle> tList = new ArrayList<Triangle>();

            Scanner reader = new Scanner(this.sceneFile);
            String line;
            String[] splitLine;
            ArrayList<String> segments = new ArrayList<String>();/* = line.split(" ");*/
            
            // read the object file
            while (reader.hasNextLine()) {
                // read the line and generate the segments
                line = reader.nextLine();
                if (line.isEmpty()) continue;
                splitLine = line.split(" ");
                segments.clear();
                for (String segment:splitLine)
                    if (!segment.equals("")) segments.add(segment);
                
                if (isNull(line) || segments.get(0).equals("#")) {
                    //System.out.println("Comment read.");
                }
                else if (segments.get(0).equals("o")) {
                    sceneTitle = segments.get(1);
                }
                else if (segments.get(0).equals("v")) {
                    vxList.add(
                        new Vertex(
                                Float.parseFloat(segments.get(1)),  // x (float)
                                Float.parseFloat(segments.get(2)),  // y (float)
                                Float.parseFloat(segments.get(3))   // z (float)
                        )
                    );
                }
                else if (segments.get(0).equals("vn")) {
                    /*vxList.add(
                        new Vertex(
                                Float.parseFloat(segments[1]),  // x (float)
                                Float.parseFloat(segments[2]),  // y (float)
                                Float.parseFloat(segments[3])   // z (float)
                        )
                    );*/
                }
                else if (segments.get(0).equals("vt")) {
                    //System.out.println("For now, we won't be needing textures");
                }
                else if (segments.get(0).equals("f")) {
                    
                    // 2 options: we could have faces of 3, 4 or more vertex:
                    
                    if (segments.size() == 4) // command + 3 vertex (1 triangle)
                    {
                        tList.add(new Triangle(
                                this.getVertexFromObjFile(vxList, segments.get(1)),
                                this.getVertexFromObjFile(vxList, segments.get(2)),
                                this.getVertexFromObjFile(vxList, segments.get(3)))
                        );
                    }
                    else if (segments.size() == 5) // command + 4 vertex (2 tri)
                    {
                        tList.add(new Triangle(
                                this.getVertexFromObjFile(vxList, segments.get(1)),
                                this.getVertexFromObjFile(vxList, segments.get(2)),
                                this.getVertexFromObjFile(vxList, segments.get(3)))
                        );
                        tList.add(new Triangle(
                                this.getVertexFromObjFile(vxList, segments.get(1)),
                                this.getVertexFromObjFile(vxList, segments.get(3)),
                                this.getVertexFromObjFile(vxList, segments.get(4)))
                        );
                    }
                    
                }
                else {
                    //System.out.println("Command unknown.");
                }
            }
            reader.close();
            
            scene = new SceneObject();
            scene.setSceneTitle(sceneTitle);
            Mesh m = new Mesh("obj read", null);
            m.setTris(tList);
            scene.addMesh(m);
            
            
        } catch (Exception e) {
            //e.printStackTrace();
            
            System.out.print("\nThere was an error reading the file!\nClosing enigne... ");
        }
        // todo: convert from vectors to triangles.
        // todo: add those triangles to the sceneObject.
        // todo (opt): read the scene title (file title) - somehow.
                
        return scene;
    }
    
    private Vertex getVertexFromObjFile(ArrayList<Vertex> vList, String source) {
        return new Vertex(vList.get(Integer.parseInt(source.split("/")[0]) - 1));
    }
}
