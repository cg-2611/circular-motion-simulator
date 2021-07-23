package circular.motion.simulator.gui;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Tutorial {

    private int width;
    private int height;

    private JFrame frame;
    private JPanel panel;

    public Tutorial() {
        this.width = 640;
        this.height = 700;

        this.frame = new JFrame();
        this.panel = new JPanel();

        initialize();
    }

    public void initialize() {
        initializeFrame();
        initializePanel();
    }

    public void initializeFrame() {
        frame.setTitle("Tutorial");
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        panel.setBounds(0,0, width, height);

        frame.add(panel);

        frame.setVisible(true);
    }

    public void initializePanel() {
        panel.setSize(width, height);
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 6, width - 12, height - 17);
        panel.add(scrollPane);

        JTextPane tutorialTextPane = new JTextPane();

        String tutorialText = getTutorialText("./src/main/resources/tutorial.txt");
        tutorialTextPane.setText(tutorialText);

        tutorialTextPane.setEditable(false);
        scrollPane.setViewportView(tutorialTextPane);
    }

    private String getTutorialText(String filename) {
        try {
            File file = new File(filename);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String fileText = "";
            String line;

            while ((line = br.readLine()) != null) {
                fileText += line + "\n";
            }

            br.close();

            return fileText;
        } catch (FileNotFoundException e) {
            return filename + " not found.";
        } catch (IOException e) {
            return "Error reading from " + filename;
        }
    }

}
