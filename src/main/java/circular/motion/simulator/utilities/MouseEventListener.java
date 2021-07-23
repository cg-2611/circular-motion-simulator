package circular.motion.simulator.utilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import circular.motion.simulator.gui.ControlPanel;
import circular.motion.simulator.gui.RenderCanvas;
import circular.motion.simulator.objects.MobileObject;
import circular.motion.simulator.objects.Moon;
import circular.motion.simulator.objects.Planet;
import circular.motion.simulator.objects.SimulatorObject;
import circular.motion.simulator.objects.Star;

public class MouseEventListener extends MouseAdapter {

    private RenderCanvas renderCanvas;
    private ControlPanel controlPanel;

    private int startX;
    private int startY;

    public MouseEventListener(RenderCanvas renderCanvas, ControlPanel controlPanel) {
        this.renderCanvas = renderCanvas;
        this.controlPanel = controlPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // loop through all the objects in the simulation and check if the users
        // mouse click is within the bounds of the object
        for (int i = 0; i < renderCanvas.getObjects().size(); i++) {
            // if the current object does contain the mouse location on the click,
            // set the selectedObject attribute in the renderCanvas to be the current object and break the loop
            // if the loop gets to the end of the array, and no objects contain the mouse click coordinates,
            // set the selectedObject attribute in the renderCanvas to null.
            if (renderCanvas.getObjects().get(i).containsPoint(e.getX(), e.getY())) {
                renderCanvas.setSelectedObject(renderCanvas.getObjects().get(i));
                break;
            } else {
                renderCanvas.setSelectedObject(null);
            }
        }

        // if the user is in "Select" mode, and they select a star object, set the start
        // coordinates to the coordinates of the star
        if (controlPanel.getMode() == 0) {
            if (renderCanvas.getSelectedObject() != null) {
                if (renderCanvas.getSelectedObject().getType().equals("Star")) {
                    startX = renderCanvas.getSelectedObject().getX();
                    startY = renderCanvas.getSelectedObject().getY();
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // if the user is in "Select" mode, and has selected a star object, update the
        // location if the star with the users mouse coordinates as the coordinates the
        // star is centered around
        if (controlPanel.getMode() == 0) {
            if (renderCanvas.getSelectedObject() != null) {
                if (renderCanvas.getSelectedObject().getType().equals("Star")) {
                    if (renderCanvas.getSelectedObject().containsPoint(e.getX(), e.getY())) {
                        updateStar(e.getX(), e.getY());
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // if the user is in "Select" mode and has selected a star object, if the placement if the
        // star is valid, the star is "dropped", however if not, the star is returned to its
        // original location
        if (controlPanel.getMode() == 0) {
            controlPanel.updateComponents();

            if (renderCanvas.getSelectedObject() != null) {
                if (renderCanvas.getSelectedObject().getType().equals("Star")) {
                    if (!placementValid(renderCanvas.getSelectedObject())) {
                        renderCanvas.getSelectedObject().setLocation(startX, startY);
                    }
                }
            }
        } else if (controlPanel.getMode() == 1) {
            // if the user has selected to add a Star object, and as long as there are no more than
            // three stars in the simulation (if there are more than three, show an error message),
            // check if the placement of the star is valid
            // if it is, add the star to the simulation, if not show an error message
            if (controlPanel.getDropdownChoice().equals("Star")) {
                if (renderCanvas.getNumberOfStars() >= 0 && renderCanvas.getNumberOfStars() < 3) {
                    SimulatorObject star = new Star(renderCanvas, e.getX(), e.getY());

                    if (placementValid(star)) {
                        renderCanvas.addObject(star);
                        renderCanvas.setSelectedObject(star);

                        renderCanvas.setNumberOfStars(renderCanvas.getNumberOfStars() + 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot place object on top of other object.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Maximum of 3 stars allowed.");
                }
            } else if (controlPanel.getDropdownChoice().equals("Planet")) {
                // if the user has selected to add a Planet object, and a star object is selected
                // (if not, show an error message), if there are no more than 2 planets orbiting the star
                // (if there are more than 2, show an error message), check if the placement of the planet
                // is valid, if so then add to the simulation, if not, show an error message
                if (renderCanvas.getSelectedObject() != null && renderCanvas.getSelectedObject().getType().equals("Star")) {
                    Star selectedObject = (Star)renderCanvas.getSelectedObject();

                    if (selectedObject.getNumberOfPlanets() >= 0 && selectedObject.getNumberOfPlanets() < 2) {
                        if (selectedObject.containsPoint(e.getX(), e.getY())) {
                            double orbitRadius = (selectedObject.getNumberOfPlanets() + 1) * 150;
                            double T = 898;

                            SimulatorObject planet = new Planet(renderCanvas,
                                                                selectedObject,
                                                                selectedObject.getX() + (int)orbitRadius,
                                                                selectedObject.getY(),
                                                                T,
                                                                orbitRadius);

                            if (placementValid(planet)) {
                                renderCanvas.addObject(planet);
                                renderCanvas.addMobileObject((MobileObject)planet);
                                selectedObject.addPlanet((Planet)planet);

                                renderCanvas.setSelectedObject(planet);
                            } else {
                                JOptionPane.showMessageDialog(null, "Cannot place object on top of other object.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Maximum of 2 planets per star allowed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Planets can only be added to stars.");
                }
            } else if (controlPanel.getDropdownChoice().equals("Moon")) {
                // if the user has selected to add a moon object, and a planet object is selected
                // (if not, show an error message), if there is not already a moon orbiting the planet
                // (if there is, show an error message), check if the placement of the moon
                // is valid, if so then add to the simulation, if not, show an error message
                if (renderCanvas.getSelectedObject() != null && renderCanvas.getSelectedObject().getType().equals("Planet")) {
                    Planet selectedObject = (Planet)renderCanvas.getSelectedObject();

                    if (selectedObject.getNumberOfMoons() >= 0 && selectedObject.getNumberOfMoons() < 1) {
                        if (selectedObject.containsPoint(e.getX(), e.getY())) {
                            double v = (selectedObject.getNumberOfMoons() + 1) * 1.2;
                            double T = 200;
                            int orbitRadius = (selectedObject.getNumberOfMoons() + 1) * 40;

                            SimulatorObject moon = new Moon(renderCanvas,
                                                            selectedObject,
                                                            selectedObject.getX() + orbitRadius,
                                                            selectedObject.getY(),
                                                            T,
                                                            v);

                            if (placementValid(moon)) {
                                renderCanvas.addObject(moon);
                                renderCanvas.addMobileObject((MobileObject)moon);
                                selectedObject.addMoon((Moon)moon);

                            } else {
                                JOptionPane.showMessageDialog(null, "Cannot place object on top of other object.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Maximum of 1 moons per planet allowed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Moons can only be added to planets.");
                }
            }
        }
    }

    public Boolean placementValid(SimulatorObject object) {
        // returns false if the object being placed will overlap any others in the simulation
        for (int i = 0; i < renderCanvas.getObjects().size(); i++) {
            if (renderCanvas.getObjects().get(i) != object && renderCanvas.getObjects().get(i).containsObject(object)) {
                return false;
            }
        }

        // return true if the object passed will not be placed on top of another
        return true;
    }

    // updates the star as the user is dragging it
    public void updateStar(int x, int y) {
        Star selectedObject = (Star)renderCanvas.getSelectedObject();

        selectedObject.setLocation(x, y);

        // call the updateLocation method which will check if the star is inside the
        // bounds of the renderCanvas and will also move any orbiting objects with it
        selectedObject.updateLocation();
    }

}
