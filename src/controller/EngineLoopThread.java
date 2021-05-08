package controller;

/**
 *
 * @author Joel
 */
public class EngineLoopThread extends Thread {
    private EngineController controller;
    
    public static final int FPS = 70;
    public static final int TPFmillis = 1000/FPS;
    
    public static double elapsedTime;
    public static int nFramesLoop;
    public static long frameCount;
    
    public EngineLoopThread(EngineController c) {
        this.controller = c;
        EngineLoopThread.elapsedTime = TPFmillis;
        EngineLoopThread.nFramesLoop = 0;
        EngineLoopThread.frameCount = 0;
    }
    
    @Override
    public void run() {
        //System.out.println("Engine loop starts now!");
        double lastCheckedTime = System.currentTimeMillis();
        
        //try {
            while(this.controller.isLoopOn()) {
                EngineLoopThread.elapsedTime = System.currentTimeMillis() - lastCheckedTime;
                //System.out.println("Elapsed Time: "+EngineLoopThread.elapsedTime);

                if (EngineLoopThread.elapsedTime >= TPFmillis) {
                    EngineLoopThread.nFramesLoop++;
                    lastCheckedTime = System.currentTimeMillis();
                    EngineLoopThread.frameCount++;
                    //System.out.println("Frame!");

                    if (EngineLoopThread.nFramesLoop >= EngineLoopThread.FPS) {
                        //System.out.println("Second!");
                        EngineLoopThread.nFramesLoop = 0;
                    }
                    this.controller.loopFunction();
                }
            }
        /*
        } catch(Exception e) {
            //e.printStackTrace();
            System.out.println("Couldn't run the loop!");
        }*/
    }
}
