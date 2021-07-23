package circular.motion.simulator.simulator;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import circular.motion.simulator.gui.ControlPanel;
import circular.motion.simulator.gui.RenderCanvas;
import circular.motion.simulator.gui.Tutorial;
import circular.motion.simulator.gui.Window;

public class Simulator implements Runnable {

    // initialize the constant that is the maximum number of frames per second the simulation can run at
    public final int FPS = 60;

    private String title;
    private int width;
    private int height;

    private Window window;
    private RenderCanvas renderCanvas;
    private ControlPanel controlPanel;

    private Boolean running;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics g;

    private static int timeInterval = -1;

    public Simulator(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        this.running = false;
    }

    public void initialize() {
        window = new Window(title, width, height);

        renderCanvas = window.getRenderCanvas();
        controlPanel = window.getControlPanel();
    }

    public static void setTimeInterval(int t) {
        timeInterval = t;
    }

    // by making the methods synchronized, they can only be executed by one thread at a time,
    // preventing start and stop being called at the same time
    public synchronized void start() {
        // prevents the thread being started if it is already running
        if (running) {
            return;
        }

        running = true;

        thread = new Thread(this);
        thread.start();

        // open the tutorial window on initial start up
        new Tutorial();
    }

    public synchronized void stop() {
        // prevents the thread being stopped if is already not running
        if (!running) {
            return;
        }

        running = false;

        try{
            // ends the thread safely
            thread.join();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // call the updateObjects method in the renderCanvas which will update the location of any object that moves
        // as an argument, the timeInterval attribute is passed in, and then incremented
        renderCanvas.updateObjects(timeInterval++);

        if (controlPanel.getSelectedMobileObject() != null) {
            controlPanel.updateAngleValues();
        }
    }

    // creates a platform that handles the rendering of the objects in the simulation (if one doesn't already exist),
    // renders the objects and then frees up memory by resetting the graphics
    public void render() {
        bs = renderCanvas.getBufferStrategy();

        if (bs == null) {
            renderCanvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        g.clearRect(0, 0, width, height);

        renderCanvas.renderObjects(g);

        bs.show();

        g.dispose();
    }

    @Override
    public void run() {
        initialize();

        // initialize the variable used to store the maximum time in nano seconds
        // allowed render and update in order to achieve 60 frames per second
        double timePerUpdate = 1000000000 / FPS;

        // initialize the variable used to store the time in nano seconds until
        // simulation needs to be updated and rendered again
        double delta = 0;

        // initialize the variables used to store the time of the current frame
        // and the time of the previous frame in nanoseconds
        long timeNow;
        long timePast = System.nanoTime();

        while (running) {
            timeNow = System.nanoTime();

            // calculate how long is needed until the simulation must be updated
            // and rendered again
            delta += (timeNow - timePast) / timePerUpdate;

            timePast = timeNow;

            if (delta >= 1) {
                // only update the simulation if it is not paused, however always render it
                if (!controlPanel.isPaused()) {
                    update();
                }

                render();

                // decrement delta so that it is ready to be used again
                // if this is not decremented it will always be greater than 1, so the
                // simulation will continuously update and render
                delta--;
            }
        }

        stop();
    }

}