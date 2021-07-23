package circular.motion.simulator.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import circular.motion.simulator.objects.MobileObject;

public class ControlPanel extends JPanel {

    private RenderCanvas renderCanvas;

    private int width;
    private int height;

    private Boolean paused;
    private int mode = 1;

    // initialize all the java swing components needed in the gui as attributes
    private JLabel lblSelectMode;
    private JButton btnSelect;
    private JButton btnEdit;
    private JLabel lblCurrentMode;

    private JComboBox<String> dropdown;
    private JLabel lblAddObject;
    private JButton btnDeleteObject;

    private JLabel lblMass;
    private JSpinner spnnrMass;
    private JLabel lblMassUnit;
    private JSlider sldrMass;

    private JLabel lblOrbitRadius;
    private JSpinner spnnrOrbitRadius;
    private JLabel lblOrbitRadiusUnit;
    private JSlider sldrOrbitRadius;

    private JLabel lblPeriod;
    private JSpinner spnnrPeriod;
    private JLabel lblPeriodUnit;
    private JSlider sldrPeriod;

    private JCheckBox cbAngularVelocity;
    private JLabel lblAngularVelocity;
    private JCheckBox cbLinearVelocity;
    private JLabel lblLinearVelocity;
    private JCheckBox cbCentripetalForce;
    private JLabel lblCentripetalForce;
    private JCheckBox cbCentripetalAcceleration;
    private JLabel lblCentripetalAcceleration;
    private JCheckBox cbFrequency;
    private JLabel lblFrequency;
    private JCheckBox cbAngleRad;
    private JLabel lblAngleRad;
    private JCheckBox cbAngleDeg;
    private JLabel lblAngleDeg;

    private JButton btnPlayPause;
    private JButton btnReset;
    private JButton btnClear;
    private JButton btnTutorial;


    private String[] objectsList = {"Star", "Planet", "Moon"};

    private DecimalFormat df = new DecimalFormat("#.##");

    private SpinnerModel spnnrNullModel = new SpinnerNumberModel(0, -1, 1, 1);
    private BoundedRangeModel sldrNullModel = new DefaultBoundedRangeModel(0, 0, -1, 1);

    private double maxStarMass = 50;
    private double minStarMass = 10;
    private double maxPlanetMass = 24;
    private double minPlanetMass = 5;
    private double maxMoonMass = 12;
    private double minMoonMass = 3;

    private double maxPlanetOrbitRadius = 700;
    private double minPlanetOrbitRadius = 10;
    private double maxMoonOrbitRadius = 100;
    private double minMoonOrbitRadius = 5;

    private double maxPlanetT = 30;
    private double minPlanetT = 1.5;
    private double maxMoonT = 10;
    private double minMoonT = 0.5;

    public ControlPanel(RenderCanvas renderCanvas, int width, int height) {
        this.renderCanvas = renderCanvas;
        this.width = width;
        this.height = height;

        paused = true;

        initialize();

        updateComponents();
    }

    public void initialize() {
        setSize(width, height);
        setLayout(null);

        setBackground(Color.GRAY);

        lblSelectMode = new JLabel("Select Mode:");
        lblSelectMode.setBounds(6, 12, 79, 16);
        add(lblSelectMode);

        btnSelect = new JButton("Select");
        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // when the button is pressed, the mode is set to 0, indicating
                // the "Select" mode, and the "Current Mode" label is updated
                mode = 0;
                lblCurrentMode.setText("Current Mode: Select");

                updateComponents();
            }
        });
        btnSelect.setBounds(6, 40, 76, 29);
        add(btnSelect);

        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // when the button is pressed, the mode is set to 1, indicating
                // the "Edit" mode, and the "Current Mode" label is updated
                mode = 1;
                lblCurrentMode.setText("Current Mode: Edit");

                updateComponents();
            }
        });
        btnEdit.setBounds(6, 81, 75, 29);
        add(btnEdit);

        lblCurrentMode = new JLabel("Current Mode: Edit"); // default mode is "Edit"
        lblCurrentMode.setBounds(6, 121, 131, 16);
        add(lblCurrentMode);

        lblAddObject = new JLabel("Add Object:");
        lblAddObject.setBounds(155, 32, 75, 16);
        add(lblAddObject);

        btnDeleteObject = new JButton("Delete Object");
        btnDeleteObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // when the delete button is pressed the current object that is selected is removed
                // and then the selected object is set to null
                if (renderCanvas.getSelectedObject() != null) {
                    renderCanvas.getSelectedObject().delete();
                    renderCanvas.setSelectedObject(null);
                }
            }
        });
        btnDeleteObject.setBounds(345, 26, 129, 29);
        add(btnDeleteObject);

        dropdown = new JComboBox<String>();
        DefaultComboBoxModel<String> objectTypes = new DefaultComboBoxModel<String>(objectsList);
        dropdown.setModel(objectTypes);
        dropdown.setBounds(236, 27, 103, 27);
        add(dropdown);

        lblMass = new JLabel("Mass x 10^24:");
        String mTooltipText = "The mass of the object, unit kilogram, kg. Used to calculate the centripetal force.";
        lblMass.setToolTipText("<html>" + mTooltipText + "</html>");
        lblMass.setBounds(155, 84, 92, 16);
        add(lblMass);

        spnnrMass = new JSpinner();
        spnnrMass.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double)spnnrMass.getValue();

                // as sliders only work with integers, multiply the spinner value by 10
                // so it can be shown on the slider
                value *= 10;
                sldrMass.setValue((int) Math.round(value));

                // change the mass of the object
                renderCanvas.getSelectedObject().setM((double)spnnrMass.getValue());

                updateValues();
            }
        });
        spnnrMass.setBounds(253, 79, 71, 26);
        add(spnnrMass);

        lblMassUnit = new JLabel("kg");
        lblMassUnit.setBounds(330, 84, 17, 16);
        add(lblMassUnit);

        sldrMass = new JSlider();
        sldrMass.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double) sldrMass.getValue();

                // as sliders only work with integers, divide the slider value by 10 to have
                // the spinner show the correct value
                value /= 10;
                spnnrMass.setValue(value);

                // change the mass of the object
                renderCanvas.getSelectedObject().setM((double)(sldrMass.getValue() / 10));

                updateValues();
            }
        });
        sldrMass.setBounds(155, 108, 319, 29);
        add(sldrMass);

        lblOrbitRadius = new JLabel("Orbit Radius x 10^9:");
        String orTooltipText = "The radius of the objects orbit, unit metres, m."
                             + "<br>"
                             + "Used to calculate the linear velocity, centripetal force and the centripetal acceleration.";
        lblOrbitRadius.setToolTipText("<html>" + orTooltipText + "</html>");
        lblOrbitRadius.setBounds(480, 81, 144, 16);
        add(lblOrbitRadius);

        spnnrOrbitRadius = new JSpinner();
        spnnrOrbitRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double) spnnrOrbitRadius.getValue();

                // as sliders only work with integers, multiply the spinner value by 100
                // so it can be shown on the slider
                sldrOrbitRadius.setValue((int)(value * 100));

                // change the orbit radius of the object
                // multiply by 10 to maintain consistency between the values and the simulation
                double v = value * 10;
                if (getSelectedMobileObject() != null) {
                    getSelectedMobileObject().setOrbitRadius(v);
                }

                updateValues();
            }
        });
        spnnrOrbitRadius.setBounds(630, 76, 71, 26);
        add(spnnrOrbitRadius);

        lblOrbitRadiusUnit = new JLabel("m");
        lblOrbitRadiusUnit.setBounds(707, 81, 43, 16);
        add(lblOrbitRadiusUnit);

        sldrOrbitRadius = new JSlider();
        sldrOrbitRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double)sldrOrbitRadius.getValue();

                // as sliders only work with integers, divide the slider value by 100 to have
                // the spinner show the correct value
                spnnrOrbitRadius.setValue(value / 100);

                // change the orbit radius of the object
                // divide by 10 to maintain consistency between the values and the simulation
                double v = value / 10;
                if (getSelectedMobileObject() != null) {
                    getSelectedMobileObject().setOrbitRadius(v);
                }

                updateValues();
            }
        });
        sldrOrbitRadius.setBounds(480, 108, 281, 29);
        add(sldrOrbitRadius);

        lblPeriod = new JLabel("Period x 10^6:");
        String periodTooltipText = "The period of the object, the time taken for a single rotation, unit seconds, s."
                                 + "<br>"
                                 + "Used to calculate the angular velocity, linear velocity, frequency and angle.";
        lblPeriod.setToolTipText("<html>" + periodTooltipText + "</html>");
        lblPeriod.setBounds(480, 11, 92, 16);
        add(lblPeriod);

        spnnrPeriod = new JSpinner();
        spnnrPeriod.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double)spnnrPeriod.getValue();

                // as sliders only work with integers, multiply the spinner value by 100
                // so it can be shown on the slider
                sldrPeriod.setValue((int)(value * 100));

                // change the period of the object
                // multiply by 100 to maintain consistency between the values and the simulation
                double T = value * 100;
                if (getSelectedMobileObject() != null) {
                    getSelectedMobileObject().setT(T);
                }

                updateValues();
            }
        });
        spnnrPeriod.setBounds(584, 7, 71, 26);
        add(spnnrPeriod);

        lblPeriodUnit = new JLabel("s");
        lblPeriodUnit.setBounds(667, 12, 17, 16);
        add(lblPeriodUnit);

        sldrPeriod = new JSlider();
        sldrPeriod.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                double value = (double) sldrPeriod.getValue();

                // as sliders only work with integers, divide the slider value by 100 to have
                // the spinner show the correct value
                spnnrPeriod.setValue(value / 100);

                // change the period of the object
                double T = value;
                if (getSelectedMobileObject() != null) {
                    getSelectedMobileObject().setT(T);
                }

                updateValues();
            }
        });
        sldrPeriod.setBounds(480, 41, 281, 29);
        add(sldrPeriod);

        cbAngularVelocity = new JCheckBox("");
        cbAngularVelocity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblAngularVelocity.getText().equals("w x 10^-7: N/A")) {
                    if (!cbAngularVelocity.isSelected()) {
                        lblAngularVelocity.setText("w x 10^-7: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getAngularVelocity());
                        lblAngularVelocity.setText("w x 10^-7: " + formattedValue + " rads^-1");
                    }
                }
            }
        });
        cbAngularVelocity.setSelected(true);
        cbAngularVelocity.setBounds(779, 6, 29, 23);
        add(cbAngularVelocity);

        lblAngularVelocity = new JLabel("w x 10^-7: rads^-1");
        lblAngularVelocity.setBounds(814, 11, 174, 16);
        String avTooltipText = "The angular velocity of the selected object, "
                             + "the angle moved in a circular path per second, unit radians per second, rads^-1."
                             + "<br>"
                             + "Calculated using the equation w = 2Pi / T, where w is the angular velocity and T is the period.";
        lblAngularVelocity.setToolTipText("<html>" + avTooltipText + "</html>");
        add(lblAngularVelocity);

        cbLinearVelocity = new JCheckBox("");
        cbLinearVelocity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblLinearVelocity.getText().equals("v x 10^4: N/A")) {
                    if (!cbLinearVelocity.isSelected()) {
                        lblLinearVelocity.setText("v x 10^4: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getLinearVelocity());
                        lblLinearVelocity.setText("v x 10^4: " + formattedValue + " ms^-1");
                    }
                }
            }
        });
        cbLinearVelocity.setSelected(true);
        cbLinearVelocity.setBounds(779, 41, 28, 23);
        add(cbLinearVelocity);

        lblLinearVelocity = new JLabel("v x 10^4: ms^-1");
        lblLinearVelocity.setBounds(813, 47, 174, 16);
        String lvTooltipText = "The linear velocity of the object, acts perpendicular to the centripetal force, "
                             + "unit metres per second, ms^-1."
                             + "<br>"
                             + "Calculated using the equation v = wr, where  v is the linear velocity, "
                             + "w is the angular velocity and r is the orbit radius.";
        lblLinearVelocity.setToolTipText("<html>" + lvTooltipText + "</html>");
        add(lblLinearVelocity);

        cbCentripetalForce = new JCheckBox("");
        cbCentripetalForce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblCentripetalForce.getText().equals("F x 10^22: N/A")) {
                    if (!cbCentripetalForce.isSelected()) {
                        lblCentripetalForce.setText("F x 10^22: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getCentripetalForce());
                        lblCentripetalForce.setText("F x 10^22: " + formattedValue + " N");
                    }
                }
            }
        });
        cbCentripetalForce.setSelected(true);
        cbCentripetalForce.setBounds(779, 76, 28, 23);
        add(cbCentripetalForce);

        lblCentripetalForce = new JLabel("F x 10^22: N");
        lblCentripetalForce.setBounds(813, 81, 174, 16);
        String cfTooltipText = "The centripetal force of the selected object, the force required to keep it in orbit, unit Newtons, N."
                             + "<br>"
                             + "Calculated using the equation F = m v^2 / r, where F is the centripetal force, "
                             + "m is the mass, v is the linear velocity and r is the orbit radius.";
        lblCentripetalForce.setToolTipText("<html>" + cfTooltipText + "</html>");
        add(lblCentripetalForce);

        cbCentripetalAcceleration = new JCheckBox("");
        cbCentripetalAcceleration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblCentripetalAcceleration.getText().equals("a x10^-3: N/A")) {
                    if (!cbCentripetalAcceleration.isSelected()) {
                        lblCentripetalAcceleration.setText("a x10^-3: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getCentripetalAcceleration());
                        lblCentripetalAcceleration.setText("a x10^-3: " + formattedValue + " ms^-2");
                    }
                }
            }
        });
        cbCentripetalAcceleration.setSelected(true);
        cbCentripetalAcceleration.setBounds(779, 111, 28, 23);
        add(cbCentripetalAcceleration);

        lblCentripetalAcceleration = new JLabel("a x10^-3: ms^-2");
        lblCentripetalAcceleration.setBounds(813, 116, 174, 16);
        String caTooltipText = "The centripetal acceleration of the selected object, "
                             + "the acceleration of the object towards the object it orbits, unit metres per second per second, ms^-2. "
                             + "<br>"
                             + "Calculated using the equation a = v^2 / r, where a is the centripetal acceleration, "
                             + "v is the linear velocity and r is the radius of the orbit.";
        lblCentripetalAcceleration.setToolTipText("<html>" + caTooltipText + "</html>");
        add(lblCentripetalAcceleration);

        cbFrequency = new JCheckBox("");
        cbFrequency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblFrequency.getText().equals("f x 10^-7: N/A")) {
                    if (!cbFrequency.isSelected()) {
                        lblFrequency.setText("f x 10^-7: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getFrequency());
                        lblFrequency.setText("f x 10^-7: " + formattedValue + " Hz");
                    }
                }
            }
        });
        cbFrequency.setSelected(true);
        cbFrequency.setBounds(1000, 5, 30, 23);
        add(cbFrequency);

        lblFrequency = new JLabel("f x 10^-7: Hz");
        lblFrequency.setBounds(1035, 9, 144, 16);
        String fTooltipText = "The frequency of the selected object, the number of orbits in one second, unit hertz, Hz."
                            + "<br>"
                            + "Calculated using the equation f = T / 2Pi, where f is the frequency and T is the period.";
        lblFrequency.setToolTipText("<html>" + fTooltipText + "</html>");
        add(lblFrequency);

        cbAngleRad = new JCheckBox("");
        cbAngleRad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblAngleRad.getText().equals("Angle: N/A")) {
                    if (!cbAngleRad.isSelected()) {
                        lblAngleRad.setText("Angle: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getAngleRad());
                        lblAngleRad.setText("Angle: " + formattedValue + " rad");
                    }
                }
            }
        });
        cbAngleRad.setSelected(true);
        cbAngleRad.setBounds(1000, 40, 30, 23);
        add(cbAngleRad);

        lblAngleRad = new JLabel("Angle: rad");
        lblAngleRad.setBounds(1035, 45, 96, 16);
        String arTooltipText = "The angle the object is currently at, "
                             + "with 0 being horizontally right of the object it orbits, unit radians, rad.";
        lblAngleRad.setToolTipText("<html>" + arTooltipText + "</html>");
        add(lblAngleRad);

        cbAngleDeg = new JCheckBox("");
        cbAngleDeg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // if the label is displaying a value, and if the checkbox is checked,
                // show the value, if not hide the value
                if (!lblAngleDeg.getText().equals("Angle: N/A")) {
                    if (!cbAngleDeg.isSelected()) {
                        lblAngleDeg.setText("Angle: ");
                    } else {
                        String formattedValue = df.format(getSelectedMobileObject().getAngleDeg());
                        lblAngleDeg.setText("Angle: " + formattedValue + " deg");
                    }
                }
            }
        });
        cbAngleDeg.setSelected(true);
        cbAngleDeg.setBounds(1000, 76, 30, 23);
        add(cbAngleDeg);

        lblAngleDeg = new JLabel("Angle: deg");
        lblAngleDeg.setBounds(1035, 81, 125, 16);
        String agTooltipText = "The angle the object is currently at, "
                             + "with 0 being horizontally right of the object it orbits, unit degrees, deg.";
        lblAngleDeg.setToolTipText("<html>" + agTooltipText + "</html>");
        add(lblAngleDeg);

        btnPlayPause = new JButton("Play");
        btnPlayPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent E) {
                if (paused) {
                    paused = false;
                    btnPlayPause.setText("Pause");
                } else {
                    paused = true;
                    btnPlayPause.setText("Play");
                }
            }
        });
        btnPlayPause.setBounds(1165, 6, 105, 29);
        add(btnPlayPause);

        btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if the simulation is paused, loop through all the mobile objects and reset them
                if (paused) {
                    for (int i = 0; i < renderCanvas.getMobileObjects().size(); i++) {
                        renderCanvas.getMobileObjects().get(i).reset();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You must pause the simulation in order to reset it.");
                }
            }
        });
        btnReset.setBounds(1165, 41, 105, 29);
        add(btnReset);

        btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paused) {
                    renderCanvas.clear();
                } else {
                    JOptionPane.showMessageDialog(null, "You must pause the simulation in order to clear it.");
                }
            }
        });
        btnClear.setBounds(1165, 76, 105, 29);
        add(btnClear);

        btnTutorial = new JButton("Tutorial");
        btnTutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create a new instance of a tutorial class
                new Tutorial();
            }
        });
        btnTutorial.setBounds(1165, 111, 105, 29);
        add(btnTutorial);
    }

    public RenderCanvas getRenderCanvas() {
        return renderCanvas;
    }

    public Boolean isPaused() {
        return paused;
    }

    public int getMode() {
        return mode;
    }

    public String getDropdownChoice() {
        return dropdown.getSelectedItem().toString();
    }

    // checks which mode the user is in and the type of object selected and disables and enables
    // the appropriate sliders, buttons and values
    public void updateComponents() {
        enableComponents(mode);

        if (renderCanvas.getSelectedObject() != null) {
            if (renderCanvas.getSelectedObject().getType().equals("Star")) {
                // set the bounds of the spinner and slider and the staring value of each
                double spnnrMassValue = renderCanvas.getSelectedObject().getM();
                SpinnerModel spnnrMassModel = new SpinnerNumberModel(spnnrMassValue,
                                                                     minStarMass,
                                                                     maxStarMass,
                                                                     0.1);
                spnnrMass.setModel(spnnrMassModel);

                int sldrMassValue = (int)(renderCanvas.getSelectedObject().getM() * 10);
                BoundedRangeModel sldrMassModel = new DefaultBoundedRangeModel(sldrMassValue,
                                                                               0,
                                                                               (int)(minStarMass * 10),
                                                                               (int)(maxStarMass * 10));
                sldrMass.setModel(sldrMassModel);

                // disable the linear velocity and angular velocity components
                spnnrOrbitRadius.setEnabled(false);
                spnnrOrbitRadius.setModel(spnnrNullModel);
                sldrOrbitRadius.setEnabled(false);
                sldrOrbitRadius.setModel(sldrNullModel);

                spnnrPeriod.setEnabled(false);
                spnnrPeriod.setModel(spnnrNullModel);
                sldrPeriod.setEnabled(false);
                sldrPeriod.setModel(sldrNullModel);
            } else {
                MobileObject selectedMobileObject = (MobileObject)renderCanvas.getSelectedObject();
                if (selectedMobileObject.getType().equals("Planet")) {
                    // set all the bounds and values of the spinners and sliders
                    double spnnrMassValue = selectedMobileObject.getM();
                    SpinnerModel spnnrMassModel = new SpinnerNumberModel(spnnrMassValue,
                                                                         minPlanetMass,
                                                                         maxPlanetMass, 0.1);
                    spnnrMass.setModel(spnnrMassModel);

                    int sldrMassValue = (int)(selectedMobileObject.getM() * 10);
                    BoundedRangeModel sldrMassModel = new DefaultBoundedRangeModel(sldrMassValue,
                                                                                   0,
                                                                                   (int)(minPlanetMass * 10),
                                                                                   (int)(maxPlanetMass * 10));
                    sldrMass.setModel(sldrMassModel);

                    double spnnrOrbitRadiusValue = selectedMobileObject.getOrbitRadius() / 10;
                    SpinnerModel spnnrOrbitRadiusModelPlanet = new SpinnerNumberModel(spnnrOrbitRadiusValue,
                                                                                      minPlanetOrbitRadius / 10,
                                                                                      maxPlanetOrbitRadius / 10,
                                                                                      0.01);
                    spnnrOrbitRadius.setModel(spnnrOrbitRadiusModelPlanet);

                    int sldrOrbitRadiusValue = (int)(selectedMobileObject.getOrbitRadius() * 10);
                    BoundedRangeModel sldrOrbitRadiusModelPlanet = new DefaultBoundedRangeModel(sldrOrbitRadiusValue,
                                                                                                0,
                                                                                                (int)(minPlanetOrbitRadius * 10),
                                                                                                (int)(maxPlanetOrbitRadius * 10));
                    sldrOrbitRadius.setModel(sldrOrbitRadiusModelPlanet);

                    double spnnrPeriodValue = selectedMobileObject.getT() / 100;
                    SpinnerModel spnnrPeriodModelPlanet = new SpinnerNumberModel(spnnrPeriodValue,
                                                                                 minPlanetT,
                                                                                 maxPlanetT,
                                                                                 0.01);
                    spnnrPeriod.setModel(spnnrPeriodModelPlanet);

                    int sldrPeriodValue = (int)(selectedMobileObject.getT());
                    BoundedRangeModel sldrPeriodModelPlanet = new DefaultBoundedRangeModel(sldrPeriodValue,
                                                                                           0,
                                                                                           (int)(minPlanetT * 100),
                                                                                           (int)(maxPlanetT * 100));
                    sldrPeriod.setModel(sldrPeriodModelPlanet);
                } else if (selectedMobileObject.getType().equals("Moon")) {
                    // set all the bounds and values of the spinners and sliders
                    double spnnrMassValue = selectedMobileObject.getM();
                    SpinnerModel spnnrMassModel = new SpinnerNumberModel(spnnrMassValue,
                                                                         minMoonMass,
                                                                         maxMoonMass,
                                                                         0.1);
                    spnnrMass.setModel(spnnrMassModel);

                    int sldrMassValue = (int)(selectedMobileObject.getM() * 10);
                    BoundedRangeModel sldrMassModel = new DefaultBoundedRangeModel(sldrMassValue,
                                                                                   0,
                                                                                   (int)(minMoonMass * 10),
                                                                                   (int)(maxMoonMass * 10));
                    sldrMass.setModel(sldrMassModel);

                    double spnnrOrbitRadiusValue = selectedMobileObject.getOrbitRadius() / 10;
                    SpinnerModel spnnrOrbitRadiusModelPlanet = new SpinnerNumberModel(spnnrOrbitRadiusValue,
                                                                                      minMoonOrbitRadius / 10,
                                                                                      maxMoonOrbitRadius / 10,
                                                                                      0.01);
                    spnnrOrbitRadius.setModel(spnnrOrbitRadiusModelPlanet);

                    int sldrOrbitRadiusValue = (int)(selectedMobileObject.getOrbitRadius() * 10);
                    BoundedRangeModel sldrOrbitRadiusModelPlanet = new DefaultBoundedRangeModel(sldrOrbitRadiusValue,
                                                                                                0,
                                                                                                (int)(minMoonOrbitRadius * 10),
                                                                                                (int)(maxMoonOrbitRadius * 10));
                    sldrOrbitRadius.setModel(sldrOrbitRadiusModelPlanet);

                    double spnnrPeriodValue = selectedMobileObject.getT() / 100;
                    SpinnerModel spnnrPeriodModelMoon = new SpinnerNumberModel(spnnrPeriodValue,
                                                                               minMoonT,
                                                                               maxMoonT,
                                                                               0.01);
                    spnnrPeriod.setModel(spnnrPeriodModelMoon);

                    int sldrPeriodValue = (int)(selectedMobileObject.getT());
                    BoundedRangeModel sldrPeriodModelMoon = new DefaultBoundedRangeModel(sldrPeriodValue,
                                                                                         0,
                                                                                         (int)(minMoonT * 100),
                                                                                         (int)(maxMoonT * 100));
                    sldrPeriod.setModel(sldrPeriodModelMoon);
                }
            }

            updateValues();
        } else {
            disableComponents();
            disableValues();
        }
    }

    // sets all the components to be enabled or disabled based on the users mode
    public void enableComponents(int currentMode) {
        if (currentMode == 0) {
            btnDeleteObject.setEnabled(true);

            spnnrMass.setEnabled(true);
            sldrMass.setEnabled(true);

            spnnrOrbitRadius.setEnabled(true);
            sldrOrbitRadius.setEnabled(true);

            spnnrPeriod.setEnabled(true);
            sldrPeriod.setEnabled(true);

            dropdown.setEnabled(false);

            cbAngularVelocity.setEnabled(true);
            cbLinearVelocity.setEnabled(true);
            cbCentripetalForce.setEnabled(true);
            cbCentripetalAcceleration.setEnabled(true);
            cbFrequency.setEnabled(true);
            cbAngleRad.setEnabled(true);
            cbAngleDeg.setEnabled(true);
        } else if (currentMode == 1) {
            btnDeleteObject.setEnabled(false);

            spnnrMass.setEnabled(false);
            sldrMass.setEnabled(false);

            spnnrOrbitRadius.setEnabled(false);
            sldrOrbitRadius.setEnabled(false);

            spnnrPeriod.setEnabled(false);
            sldrPeriod.setEnabled(false);

            dropdown.setEnabled(true);

            cbAngularVelocity.setEnabled(false);
            cbLinearVelocity.setEnabled(false);
            cbCentripetalForce.setEnabled(false);
            cbCentripetalAcceleration.setEnabled(false);
            cbFrequency.setEnabled(false);
            cbAngleRad.setEnabled(false);
            cbAngleDeg.setEnabled(false);
        }
    }

    // sets all the components to be disabled
    public void disableComponents() {
        btnDeleteObject.setEnabled(false);

        spnnrMass.setEnabled(false);
        spnnrMass.setModel(spnnrNullModel);
        sldrMass.setEnabled(false);
        sldrMass.setModel(sldrNullModel);

        spnnrOrbitRadius.setEnabled(false);
        spnnrOrbitRadius.setModel(spnnrNullModel);
        sldrOrbitRadius.setEnabled(false);
        sldrOrbitRadius.setModel(sldrNullModel);

        spnnrPeriod.setEnabled(false);
        spnnrPeriod.setModel(spnnrNullModel);
        sldrPeriod.setEnabled(false);
        sldrPeriod.setModel(sldrNullModel);
    }

    public MobileObject getSelectedMobileObject(){
        // if the selected object has type "Planet" or "Moon", return the selectedObject as a MobileObject
        if (renderCanvas.getSelectedObject() != null) {
            if (renderCanvas.getSelectedObject().getType().equals("Planet")
            || renderCanvas.getSelectedObject().getType().equals("Moon")) {
                return (MobileObject)renderCanvas.getSelectedObject();
            }
        }
        return null;
    }

    // calculates the values for the properties of the selected object and updates the labels
    public void updateValues() {
        if (renderCanvas.getSelectedObject() != null) {
            if (renderCanvas.getSelectedObject().getType().equals("Star")) {
                disableValues();
            } else if (renderCanvas.getSelectedObject().getType().equals("Planet")
                   || renderCanvas.getSelectedObject().getType().equals("Moon")) {
                if (getSelectedMobileObject() != null) {
                    MobileObject selectedMobileObject = getSelectedMobileObject();

                    // the update value is not shown if the respective checkbox is not checked
                    if (cbAngularVelocity.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getAngularVelocity());
                        lblAngularVelocity.setText("w x 10^-7: " + formattedValue + " rads^-1");
                    }

                    if (cbLinearVelocity.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getLinearVelocity());
                        lblLinearVelocity.setText("v x 10^4: " + formattedValue + " ms^-1");
                    }

                    if (cbCentripetalForce.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getCentripetalForce());
                        lblCentripetalForce.setText("F x 10^22: " + formattedValue + " N");
                    }

                    if (cbCentripetalAcceleration.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getCentripetalAcceleration());
                        lblCentripetalAcceleration.setText("a x10^-3: " + formattedValue + " ms^-2");
                    }

                    if (cbFrequency.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getFrequency());
                        lblFrequency.setText("f x 10^-7: "+ formattedValue + " Hz");
                    }

                    if (cbAngleRad.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getAngleRad());
                        lblAngleRad.setText("Angle: " + formattedValue + " rad");
                    }

                    if (cbAngleDeg.isSelected()) {
                        String formattedValue = df.format(selectedMobileObject.getAngleDeg());
                        lblAngleDeg.setText("Angle: " + formattedValue + " deg");
                    }
                }
            }
        }
    }

    // sets all the values to N/A and disables the checkboxes
    public void disableValues() {
        lblAngularVelocity.setText("w x 10^-7: N/A");
        lblLinearVelocity.setText("v x 10^4: N/A");
        lblCentripetalForce.setText("F x 10^22: N/A");
        lblCentripetalAcceleration.setText("a x10^-3: N/A");
        lblFrequency.setText("f x 10^-7: N/A");
        lblAngleRad.setText("Angle: N/A");
        lblAngleDeg.setText("Angle: N/A");

        cbAngularVelocity.setEnabled(false);
        cbLinearVelocity.setEnabled(false);
        cbCentripetalForce.setEnabled(false);
        cbCentripetalAcceleration.setEnabled(false);
        cbFrequency.setEnabled(false);
        cbAngleRad.setEnabled(false);
        cbAngleDeg.setEnabled(false);
    }

    // sets the value of the angle labels to the angle of the selected object
    public void updateAngleValues() {
        if (renderCanvas.getSelectedObject() != null && getSelectedMobileObject() != null) {
            if (cbAngleRad.isSelected()) {
                String formattedValue = df.format(getSelectedMobileObject().getAngleRad());
                lblAngleRad.setText("Angle: " + formattedValue + " rad");
            }

            if (cbAngleDeg.isSelected()) {
                String formattedValue =  df.format(getSelectedMobileObject().getAngleDeg());
                lblAngleDeg.setText("Angle: " + formattedValue + " deg");
            }
        }
    }

}
