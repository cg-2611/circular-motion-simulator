package circular.motion.simulator.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import circular.motion.simulator.utilities.MouseEventListener;

public class Window {

    private String title;
    private int width;
    private int height;

    private JFrame frame;

    private RenderCanvas renderCanvas;
    private ControlPanel controlPanel;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        frame = new JFrame();

        renderCanvas = new RenderCanvas(width, height - 170);

        controlPanel = new ControlPanel(renderCanvas, width, 170);

        // add an instance of the MouseEventListener class to the renderCanvas so that
        // renderCanvas mouse interactions can be processed
        renderCanvas.addMouseListener(new MouseEventListener(renderCanvas, controlPanel));

        // add an instance of the MouseEventListener class to the renderCanvas so that the
        // mouseDragged method will be called
        renderCanvas.addMouseMotionListener(new MouseEventListener(renderCanvas, controlPanel));

        initialize();
    }

    public void initialize() {
        frame.setTitle(title);
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        // give the renderCanvas and controlPanel coordinates and a size within the window
        renderCanvas.setBounds(0, 0, renderCanvas.getWidth(), renderCanvas.getHeight());
        controlPanel.setBounds(0, renderCanvas.getHeight(), width, height - renderCanvas.getHeight());

        frame.add(renderCanvas);
        frame.add(controlPanel);

        frame.setVisible(true);
    }

    public RenderCanvas getRenderCanvas() {
        return renderCanvas;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

}
