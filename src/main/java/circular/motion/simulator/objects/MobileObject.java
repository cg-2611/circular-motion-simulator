package circular.motion.simulator.objects;

import java.awt.Graphics;

import circular.motion.simulator.gui.RenderCanvas;
import circular.motion.simulator.simulator.Simulator;
import circular.motion.simulator.utilities.Calculator;
import circular.motion.simulator.utilities.CircularQueue;

public abstract class MobileObject extends SimulatorObject {

    protected double orbitRadius;
    protected double T;

    protected int xOrigin;
    protected int yOrigin;

    protected double w;
    protected double v;
    protected double F;
    protected double a;
    protected double f;

    protected double theta;

    protected int queueLength;

    protected CircularQueue<Integer> prevX;
    protected CircularQueue<Integer> prevY;

    protected int numberOfOrbits;

    public MobileObject(RenderCanvas renderCanvas, int x, int y) {
        super(renderCanvas, x, y);

        numberOfOrbits = 0;
    }

    public double getOrbitRadius() {
        return orbitRadius;
    }

    public double getT() {
        return T;
    }

    public double getAngularVelocity() {
        this.w = Calculator.calculateAngularVelocity(T);
        return w;
    }

    public double getLinearVelocity() {
        this.v = Calculator.calculateLinearVelocity(orbitRadius, T);
        return v;
    }

    public double getCentripetalForce() {
        this.F = Calculator.calculateCentripetalForce(m, v, orbitRadius);
        return F;
    }

    public double getCentripetalAcceleration() {
        this.a = Calculator.calculateCentripetalAcceleration(v, orbitRadius);
        return a;
    }

    public double getFrequency() {
        this.f = Calculator.calculateFrequency(T);
        return f;
    }

    public double getAngleRad() {
        if (theta > (2 * Math.PI * (numberOfOrbits + 1))) {
            numberOfOrbits++;
        }

        double thetaValue = theta - (2 * Math.PI * numberOfOrbits);

        return thetaValue;
    }

    public double getAngleDeg() {
        return Math.toDegrees(getAngleRad());
    }

    public CircularQueue<Integer> getPrevX() {
        return prevX;
    }

    public CircularQueue<Integer> getPrevY() {
        return prevY;
    }

    // override the setLocation method so that whenever the location of a MobileObject is
    // changed, the previous one is enqueued into the CircularQueue objects.
    @Override
    public void setLocation(int x, int y) {
        prevX.enqueue(this.x);
        prevY.enqueue(this.y);

        super.setLocation(x, y);
    }

    public void setOrbitRadius(double orbitRadius) {
        this.orbitRadius = orbitRadius;

        deleteTrail();
    }

    public void setT(double T) {
        this.T = T;

        deleteTrail();

        numberOfOrbits = 0;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void reset() {
        deleteTrail();

        Simulator.setTimeInterval(-1);
    }

    // the update method will be overwritten and called by all subclasses, it will perform the collision
    // detection after the object has been updated
    public void update(int t) {
        for (int i = 0; i < renderCanvas.getObjects().size(); i++) {
            SimulatorObject object = renderCanvas.getObjects().get(i);

            if (object != this && object.containsObject(this)) {
                // if two objects of the same type collide, both are deleted
                // if two different objects collide, the objects will be deleted
                // according to this hierarchy:
                // Star > Planet > Moon
                // where the lesser object is deleted
                if (object.getType().equals("Star") && this.getType().equals("Planet")) {
                    this.delete();
                } else if (object.getType().equals("Planet") && this.getType().equals("Planet")) {
                    object.delete();
                    this.delete();
                } else if (object.getType().equals("Planet") && this.getType().equals("Moon")) {
                    this.delete();
                } else if (object.getType().equals("Moon") && this.getType().equals("Moon")) {
                    object.delete();
                    this.delete();
                } else if (object.getType().equals("Star") && this.getType().equals("Moon")) {
                    this.delete();
                }

                renderCanvas.setSelectedObject(null);
            }
        }
    }


    // the deleteTrail method re-initializes the queues
    public void deleteTrail() {
        prevX = new CircularQueue<Integer>(queueLength);
        prevY = new CircularQueue<Integer>(queueLength);
    }

    // the drawTrail method will be overwritten in the Planet and Moon classes
    public abstract void drawTrail(Graphics g);
}
