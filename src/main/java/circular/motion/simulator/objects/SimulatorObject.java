package circular.motion.simulator.objects;

import java.awt.Graphics;

import circular.motion.simulator.gui.RenderCanvas;

public abstract class SimulatorObject {

    protected RenderCanvas renderCanvas;

    protected String type;

    protected int x;
    protected int y;
    protected int r;

    protected double m;

    public SimulatorObject(RenderCanvas renderCanvas, int x, int y) {
        this.renderCanvas = renderCanvas;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        r = (int)m;
        return r;
    }

    public double getM() {
        return m;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setM(double m) {
        this.m = m;
        this.r = (int) m;
    }

    // returns true or false depending on whether the point with coordinates (x, y) is within the object
    public Boolean containsPoint(int x, int y) {
        // calculate the distance between the centre of the object and the point (x, y)
        int totalDistance = (int) Math.sqrt(Math.pow(this.x - x, 2) +
                                            Math.pow(this.y - y, 2));

        int totalRadii = (int) this.r;

        // if the distance between the object and the point is less than the object's radius,
        // then the point is inside the object
        return totalDistance < totalRadii;
    }

    // returns true or false depending on whether the object passed in has collided with the object
    public Boolean containsObject(SimulatorObject object) {
        // calculate the distance between the centres of the two objects
        int totalDistance = (int) Math.sqrt(Math.pow(this.x - object.getX(), 2) +
                                            Math.pow(this.y - object.getY(), 2));

        // calculate the sum of the radii of the objects
        int totalRadii = (int) this.r + object.getR();

        // if the distance between the centres of the objects is less than the total combined radii
        // of the objects then return true since that means that the objects have collided
        return totalDistance < totalRadii;
    }

    // draw method that will be overwritten in the Star, Planet and Moon classes
    public abstract void draw(Graphics g);

    // delete method that will be overwritten in the Star, Planet and Moon classes
    public abstract void delete();

}
