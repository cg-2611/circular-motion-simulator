package circular.motion.simulator.gui;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import circular.motion.simulator.objects.MobileObject;
import circular.motion.simulator.objects.SimulatorObject;
import circular.motion.simulator.simulator.Simulator;

public class RenderCanvas extends Canvas {

    private int width;
    private int height;

    private ArrayList<SimulatorObject> objects;
    private ArrayList<MobileObject> mobileObjects;

    private SimulatorObject selectedObject;

    private int numberOfStars = 0;

    public RenderCanvas(int width, int height) {
        this.width = width;
        this.height = height;

        objects = new ArrayList<SimulatorObject>();
        mobileObjects = new ArrayList<MobileObject>();

        setBackground(Color.BLACK);
    }

    public ArrayList<SimulatorObject> getObjects() {
        return objects;
    }

    public ArrayList<MobileObject> getMobileObjects() {
        return mobileObjects;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SimulatorObject getSelectedObject() {
        return selectedObject;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setSelectedObject(SimulatorObject selectedObject) {
        this.selectedObject = selectedObject;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public void addObject(SimulatorObject object) {
        objects.add(object);
    }

    public void removeObject(SimulatorObject object) {
        objects.remove(object);
    }

    public void addMobileObject(MobileObject object) {
        mobileObjects.add(object);
    }

    public void removeMobileObject(MobileObject object) {
        mobileObjects.remove(object);
    }

    public void clear() {
        // loop through all the objects in the simulation and delete each one
        for (int i = objects.size() - 1; i >=0 ; i--) {
            objects.get(i).delete();
        }

        selectedObject = null;

        // reset the simulator time interval so the simulator behaves as though it had just been opened
        Simulator.setTimeInterval(-1);
    }

    public void updateObjects(int t) {
        // loop through all the mobile objects in the simulation and update their position.
        for (int i = 0; i < mobileObjects.size(); i++) {
            mobileObjects.get(i).update(t);
        }
    }

    public void renderObjects(Graphics g) {
        // loop through all the mobile objects in the simulation and render their trail
        // this is done before rendering the objects so that they dont overlap over the objects
        for (int i = 0; i < mobileObjects.size(); i++) {
            mobileObjects.get(i).drawTrail(g);
        }

        // loop through all the objects in the simulation and draw each one on the renderCanvas
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).draw(g);
        }

        // if the user has selected an object render a red circle with no fill colour using the coordinates
        // of the selected object and the radius of the selected object
        if (selectedObject != null) {
            Graphics2D g2d = (Graphics2D)g;

            g2d.setColor(Color.RED);

            // increase the thickness of the line
            g2d.setStroke(new BasicStroke(3));

            g2d.drawOval(selectedObject.getX() - selectedObject.getR(),
                         selectedObject.getY() - selectedObject.getR(),
                         selectedObject.getR() * 2,
                         selectedObject.getR() * 2);
        }
    }

}
