package circular.motion.simulator.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import circular.motion.simulator.gui.RenderCanvas;
import circular.motion.simulator.utilities.CircularQueue;

public class Moon extends MobileObject {

    private Planet planet;

    public Moon(RenderCanvas renderCanvas, Planet planet, int x, int y, double T, double v) {
        super(renderCanvas, x, y);

        this.type = "Moon";

        this.m = 6;

        this.planet = planet;
        this.orbitRadius = planet.getR() + 30;
        this.T = T;
        this.v = v;

        this.xOrigin = planet.getXOrigin() + (int)orbitRadius;
        this.yOrigin = planet.getYOrigin();

        // queue length calculated using a ratio that produces a trail of roughly 3/4 of the object's orbit
        // circumference based on how fast the object is moving
        queueLength = (int)Math.round((21 / 4) * (1 / ((2 * Math.PI) / planet.getT())));

        this.prevX = new CircularQueue<Integer>(queueLength);
        this.prevY = new CircularQueue<Integer>(queueLength);
    }

    public Planet getPlanet() {
        return planet;
    }

    public int getXOrigin() {
        xOrigin = planet.getXOrigin() + (int)orbitRadius;
        return xOrigin;
    }

    public int getYOrigin() {
        yOrigin = planet.getYOrigin();
        return yOrigin;
    }

    public void updateLocation(Planet planet) {
        deleteTrail();

        // the coordinates of the object are updated in relation to the planet's new position
        x = (int)(planet.getX() + Math.cos(theta) * orbitRadius);
        y = (int)(planet.getY() + Math.sin(theta) * orbitRadius);
    }

    @Override
    public void reset() {
        super.reset();

        x = getXOrigin();
        y = getYOrigin();
    }

    // override the update method so that the new location of the moon is relative to the planet it is orbiting
    @Override
    public void update(int t) {
        // calculate the angle in radians the moon needs to be at, at the given time
        double w = (2 * Math.PI) / T;
        theta = w * t;

        // calculate the new coordinates of the planet
        double newX = planet.getX() + (Math.cos(theta) * orbitRadius);
        double newY = planet.getY() + (Math.sin(theta) * orbitRadius);

        super.setLocation((int)newX, (int)newY);

        // perform the collision detection
        super.update(t);
    }

    // override the draw method so that the moon is a different color to the other types of object
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // draw a grey circle with centre (x, y) and radius r
        g2d.setColor(Color.GRAY);
        g2d.fillOval(x - getR(), y - getR(), getR() * 2, getR() * 2);
    }

    // override the delete method so that this specific object is deleted
    @Override
    public void delete() {
        // remove the object from the simulation
        renderCanvas.removeObject(this);
        renderCanvas.removeMobileObject(this);

        // update the Planets number of moons
        planet.setNumberOfMoons(planet.getNumberOfMoons() - 1);

        renderCanvas.setSelectedObject(null);
    }


    // override the drawTrail method so that the trail of the planet is a different colour to the trail of the moon
    @Override
    public void drawTrail(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // for every coordinate in the prevX and prevY queues, draw an orange circle of radius 3
        g2d.setColor(Color.ORANGE);
        for (int i = 0; i < prevX.size(); i++) {
            g2d.fillOval(prevX.get(i) - 1, prevY.get(i) - 1, 3, 3);
        }
    }

    // override the deleteTrail method so that the trail of the moon is removed
    @Override
    public void deleteTrail() {
        // calculate the length of the new queues
        queueLength = (int)Math.round((21 / 4) * (1 / ((2 * Math.PI) / planet.getT())));

        super.deleteTrail();
    }

}
