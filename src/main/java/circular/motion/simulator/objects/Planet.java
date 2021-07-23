package circular.motion.simulator.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import circular.motion.simulator.gui.RenderCanvas;
import circular.motion.simulator.utilities.CircularQueue;

public class Planet extends MobileObject{

    private Star star;

    private ArrayList<Moon> moons;

    private int numberOfMoons;

    public Planet(RenderCanvas renderCanvas, Star star, int x, int y, double T, double r) {
        super(renderCanvas, x, y);

        this.type = "Planet";

        this.m = 12;

        this.star  = star;
        this.orbitRadius = x - star.getX();
        this.T = T;
        this.orbitRadius = r;

        this.xOrigin = star.getX() + (int)orbitRadius;
        this.yOrigin = star.getY();

        moons = new ArrayList<Moon>();

        // queue length calculated using a ratio that produces a trail of roughly 3/4 of the object's orbit
        // circumference based on how fast the object is moving
        queueLength = (int)Math.round((21 / 4) * (1 / ((2 * Math.PI) / T)));

        prevX = new CircularQueue<Integer>(queueLength);
        prevY = new CircularQueue<Integer>(queueLength);
    }

    public Star getStar() {
        return star;
    }

    public ArrayList<Moon> getMoons(){
        return moons;
    }

    public int getNumberOfMoons() {
        return numberOfMoons;
    }

    public int getXOrigin() {
        xOrigin = star.getX() + (int)orbitRadius;
        return xOrigin;
    }

    public int getYOrigin() {
        yOrigin = star.getY();
        return yOrigin;
    }

    @Override
    public void setT(double T) {
        super.setT(T);

        // loop through all moons and delete the trail as well
        for (int i = 0; i < moons.size(); i++) {
            moons.get(i).deleteTrail();
        }
    }

    @Override
    public void setOrbitRadius(double orbitRadius) {
        super.setOrbitRadius(orbitRadius);

        // loop through all moons and delete the trail as well
        for (int i = 0; i < moons.size(); i++) {
            moons.get(i).deleteTrail();
        }
    }

    public void setNumberOfMoons(int numberOfMoons) {
        this.numberOfMoons = numberOfMoons;
    }

    public void addMoon(Moon moon) {
        moons.add(moon);
        numberOfMoons++;
    }

    public void removeMoon(Moon moon) {
        moons.remove(moon);
        numberOfMoons--;
    }

    public void updateLocation(Star star) {
        deleteTrail();

        // the coordinates of the object are updated in relation to the star's new position
        x = (int)(star.getX() + Math.cos(theta) * orbitRadius);
        y = (int)(star.getY() + Math.sin(theta) * orbitRadius);
    }

    @Override
    public void reset() {
        super.reset();

        x = getXOrigin();
        y = getYOrigin();
    }

    // override the update method so that the new location of the planet is relative to the star it is orbiting
    @Override
    public void update(int t) {
        // calculate the angle, in radians the planet needs to be at, at the given time
        double w = (2 * Math.PI) / T;
        theta = w * t;

        // calculate the new coordinates of the planet.
        double newX = star.getX() + (Math.cos(theta) * orbitRadius);
        double newY = star.getY() + (Math.sin(theta) * orbitRadius);

        super.setLocation((int)newX, (int)newY);

        // perform the collision detection
        super.update(t);
    }

    // override the draw method so that the planet is a different color to the other types of object
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // draw a blue circle with centre (x, y) and radius r
        g2d.setColor(Color.BLUE);
        g2d.fillOval(x - getR(), y - getR(), getR() * 2, getR() * 2);
    }

    // override the delete method so that when the object is removed, so are any other objects that orbit it
    @Override
    public void delete() {
        renderCanvas.removeObject(this);
        renderCanvas.removeMobileObject(this);

        // loop through all the moons that orbit and delete them as well
        for (int i = moons.size() - 1; i >= 0; i--) {
            moons.get(i).delete();
            removeMoon(moons.get(i));
        }

        star.setNumberOfPlanets(star.getNumberOfPlanets() - 1);

        renderCanvas.setSelectedObject(null);
    }

    // override the drawTrail method so that the trail of the planet is a different colour to the trail of the moon
    @Override
    public void drawTrail(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // for every coordinate in the prevX and prevY queues, draw a white circle of radius 3
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < prevX.size(); i++) {
            g2d.fillOval(prevX.get(i) - 1, prevY.get(i) - 1, 3, 3);
        }
    }

    // override the deleteTrail method so that the trail of the moon is removed
    @Override
    public void deleteTrail() {
        // calculate the length of the new queues
        queueLength = (int)Math.round((21 / 4) * (1 / ((2 * Math.PI) / T)));

        super.deleteTrail();
    }

}
