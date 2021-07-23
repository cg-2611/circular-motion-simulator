package circular.motion.simulator.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import circular.motion.simulator.gui.RenderCanvas;

public class Star extends SimulatorObject {

    private ArrayList<Planet> planets;

    private int numberOfPlanets = 0;

    public Star(RenderCanvas renderCanvas, int x, int y) {
        super(renderCanvas, x, y);

        this.type = "Star";

        this.m = 25;

        planets = new ArrayList<Planet>();
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public int getNumberOfPlanets() {
        return numberOfPlanets;
    }

    public void setNumberOfPlanets(int numberOfPlanets) {
        this.numberOfPlanets = numberOfPlanets;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
        numberOfPlanets++;
    }

    public void removePlanet(Planet planet) {
        planets.remove(planet);
        numberOfPlanets--;
    }

    public void updateLocation() {
        int newX = this.x;
        int newY = this.y;

        // checks if the current location of the star is within the renderCanvas
        // if it is not, then the coordinates are updated to be within the bounds
         if (this.x - this.r < 0) {
            newX = this.r + 1;
        }

        if (this.x + this.r > renderCanvas.getWidth()) {
            newX = renderCanvas.getWidth() - this.r + 1;
        }

        if (this.y - this.r < 0) {
            newY = this.r + 1;
        }

        if (this.y + this.r > renderCanvas.getHeight()) {
            newY = renderCanvas.getHeight() - this.r + 1;
        }

        setLocation(newX, newY);

        // loop through all the planets and moons and update their locations relative
        // to the star so that they move with it
        for (int i = 0; i < planets.size(); i++) {
            planets.get(i).updateLocation(this);

            for (int j = 0; j < planets.get(i).getMoons().size(); j++) {
                planets.get(i).getMoons().get(j).updateLocation(planets.get(i));
            }
        }
    }

    // override the draw method so that the star is a different color to the other
    // types of object
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // Draw a yellow circle with centre (x, y) and radius r.
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x - getR(), y - getR(), getR() * 2, getR() * 2);
    }

    // override the delete method so that when the object is removed, so are any other
    // objects that orbit it
    @Override
    public void delete() {
        renderCanvas.removeObject(this);

        renderCanvas.setNumberOfStars(renderCanvas.getNumberOfStars() - 1);

        // loop through all the planets that orbit and delete them as well
        for (int i = planets.size() - 1; i >= 0; i--) {
            planets.get(i).delete();
            removePlanet(planets.get(i));
        }

        renderCanvas.setSelectedObject(null);
    }

}
