 package controller;

/**
 *
 * @author Joel
 */
public class EngineLoopThread extends Thread {
    private EngineController controller;
    
    public static final int FPS = 70;
    public static final int TPFmillis = 1000/FPS;
    
    public static double currentLoopTime;
    public static double lastTimeLoopCheck;
    public static double framingTime;
    public static double elapsedTime;
    public static int nFramesLoop;
    public static long frameCount;
    
    public EngineLoopThread(EngineController c) {
        this.controller = c;
        EngineLoopThread.elapsedTime = TPFmillis;
        EngineLoopThread.nFramesLoop = 0;
        EngineLoopThread.frameCount = 0;
        EngineLoopThread.lastTimeLoopCheck = 0;
        EngineLoopThread.currentLoopTime = 0;
        EngineLoopThread.framingTime = 0;
    }
    
    @Override
    public void run() {
        //System.out.println("Engine loop starts now!");
        double lastCheckedTime = System.currentTimeMillis();
        
        //try {
            while(this.controller.isLoopOn()) {
                EngineLoopThread.currentLoopTime = System.currentTimeMillis();
                EngineLoopThread.lastTimeLoopCheck = EngineLoopThread.currentLoopTime - lastCheckedTime;
                //System.out.println("Elapsed Time: "+EngineLoopThread.elapsedTime);
                EngineLoopThread.framingTime = EngineLoopThread.currentLoopTime;

                if (EngineLoopThread.lastTimeLoopCheck >= TPFmillis) {
                    EngineLoopThread.elapsedTime = EngineLoopThread.lastTimeLoopCheck;
                    EngineLoopThread.nFramesLoop++;
                    lastCheckedTime = System.currentTimeMillis();
                    EngineLoopThread.frameCount++;
                    //System.out.println("Frame!");

                    if (EngineLoopThread.nFramesLoop >= EngineLoopThread.FPS) {
                        //System.out.println("Second!");
                        EngineLoopThread.nFramesLoop = 0;
                    }
                    this.controller.loopFunction();
                    EngineLoopThread.framingTime = System.currentTimeMillis() - EngineLoopThread.framingTime;
                }
            }
        /*
        } catch(Exception e) {
            //e.printStackTrace();
            System.out.println("Couldn't run the loop!");
        }*/
    }
}
