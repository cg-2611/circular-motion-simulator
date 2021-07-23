package circular.motion.simulator.simulator;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Launcher {

    // get height of user's screen
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double screenHeight = screenSize.getHeight();

    private static final String TITLE = "Circular Motion Simulator";
    private static final int WIDTH = 1280;

    // calculate the height the simulator should be based on the height of the screen
    private static final int HEIGHT = (int)((screenHeight * 6) / 7);

    public static void main(String[] args) {
        try {
        	// set the style of all the java swing components to look like they are the
        	// same as the components native to the user's operating system
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // create a Simulator object assign it a new instance of a Simulator
            Simulator simulator = new Simulator(TITLE, WIDTH, HEIGHT);

            SwingUtilities.invokeLater(() -> {
                // start the thread that the simulator will run on
                simulator.start();
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
